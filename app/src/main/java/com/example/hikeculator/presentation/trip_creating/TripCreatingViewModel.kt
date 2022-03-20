package com.example.hikeculator.presentation.trip_creating

import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.domain.common.NutritionalCalculator
import com.example.hikeculator.domain.entities.DayMeal
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripSeason
import com.example.hikeculator.domain.enums.TripType
import com.example.hikeculator.domain.interactors.MemberGroupInteractor
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.presentation.common.TripDateFormat
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class TripCreatingViewModel(
    private val tripInteractor: TripInteractor,
    private val memberInteractor: MemberGroupInteractor,
    private val tripDayInteractor: TripDayInteractor,
    private val tripCreatorUid: String,
) : ITripCreatingViewModel() {

    override val seasonState = MutableStateFlow<TripSeason?>(value = null)
    override val categoryState = MutableStateFlow<TripDifficultyCategory?>(value = null)
    override val typeState = MutableStateFlow<TripType?>(value = null)
    override val dateState = MutableStateFlow<Pair<Long, Long>?>(value = null)
    override val addedMembers = MutableStateFlow<Set<User>>(value = emptySet())
    override val searchedMembers = MutableStateFlow<Set<User>>(value = emptySet())
    override val problemMessage = MutableSharedFlow<Int>()

    override fun setSeason(season: TripSeason?) {
        seasonState.value = season
    }

    override fun setCategory(category: TripDifficultyCategory) {
        categoryState.value = category
    }

    override fun setType(type: TripType) {
        typeState.value = type
    }

    override fun setDate(pickedDate: Pair<Long, Long>) {
        dateState.value = pickedDate
    }

    override fun addTripMember(member: User) {
        val updatedMemberCollection = mutableSetOf<User>().apply {
            addAll(addedMembers.value)
            add(member)
        }
        addedMembers.value = updatedMemberCollection
    }

    override fun removeTripMember(member: User) {
        val updatedMemberCollection = mutableSetOf<User>().apply {
            addAll(addedMembers.value)
            remove(member)
        }
        addedMembers.value = updatedMemberCollection
    }

    override fun clearSearchedMemberList() {
        searchedMembers.value = emptySet()
    }

    override fun searchByEmail(text: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            problemMessage.tryEmit(R.string.problem_with_member_search)
        }

        viewModelScope.launch(exceptionHandler) {
            searchedMembers.value = memberInteractor.searchTripMembers(email = text)
        }
    }

    override fun createTrip(
        name: String,
        startDate: Long,
        endDate: Long,
        type: TripType,
        difficultyCategory: TripDifficultyCategory,
        season: TripSeason,
    ) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            problemMessage.tryEmit(R.string.poblem_with_trip_creating)
        }

        viewModelScope.launch(exceptionHandler) {
            val tripCreator = memberInteractor.fetchTripMember(userUid = tripCreatorUid)

            if (tripCreator != null) {
                addTripMember(member = tripCreator)

                val dayQuantity = TripDateFormat.getDayQuantity(
                    startDate = startDate,
                    endDate = endDate
                )

                val calorieNorm = NutritionalCalculator.calculateTripCalorieNorm(
                    type = type,
                    difficultyCategory = difficultyCategory,
                    season = season,
                    dayQuantity = dayQuantity,
                    members = addedMembers.value.toTypedArray()
                )

                val tripId = UUID.randomUUID().toString()

                val trip = Trip(
                    id = tripId,
                    name = name,
                    startDate = startDate,
                    endDate = endDate,
                    memberUids = addedMembers.value.map { member -> member.uid }.toSet(),
                    totalCalories = calorieNorm,
                    type = type,
                    difficultyCategory = difficultyCategory,
                    season = season
                )

                tripInteractor.insertTrip(trip = trip)
                createEmptyTripDays(tripId = tripId, startDate = startDate, endDate = endDate)
                clearTripCreatingState()
            } else {
                problemMessage.tryEmit(value = R.string.message_failed_to_create_the_trip)
            }
        }
    }

    override fun clearTripCreatingState() {
        seasonState.value = null
        categoryState.value = null
        typeState.value = null
        dateState.value = null
        addedMembers.value = emptySet()
        searchedMembers.value = emptySet()
    }

    override fun createEmptyTripDays(tripId: String, startDate: Long, endDate: Long) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            problemMessage.tryEmit(R.string.poblem_with_day_creating)
        }

        val dayQuantity = TripDateFormat.getDayQuantity(startDate = startDate, endDate = endDate)

        (0 until dayQuantity).forEach { dayOrdinal ->
            viewModelScope.launch(context = Dispatchers.IO + exceptionHandler) {
                val date = getDateByDayOrdinal(startDate = startDate, dayOrdinal = dayOrdinal)

                tripDayInteractor.insertTripDay(
                    tripId = tripId,
                    tripDay = generateEmptyTripDay(date = date)
                )
            }
        }
    }

    private fun getDateByDayOrdinal(startDate: Long, dayOrdinal: Int): Long {
        return startDate + TimeUnit.DAYS.toMillis(dayOrdinal.toLong())
    }

    private fun generateEmptyTripDay(date: Long): TripDay = TripDay(
        id = UUID.randomUUID().toString(),
        date = date,
        breakfast = createEmptyDayMeal(),
        lunch = createEmptyDayMeal(),
        dinner = createEmptyDayMeal(),
        snack = createEmptyDayMeal(),
    )

    private fun createEmptyDayMeal(): DayMeal = DayMeal(products = emptyList())
}