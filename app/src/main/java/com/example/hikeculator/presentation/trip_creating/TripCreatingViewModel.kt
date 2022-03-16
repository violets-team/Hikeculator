package com.example.hikeculator.presentation.trip_creating

import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.domain.common.NutritionalCalculator
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripSeason
import com.example.hikeculator.domain.enums.TripType
import com.example.hikeculator.domain.interactors.MemberGroupInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.presentation.common.TripDateFormat
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class TripCreatingViewModel(
    private val tripInteractor: TripInteractor,
    private val memberInteractor: MemberGroupInteractor,
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
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            TODO()
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
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            TODO("Handle the exception here")
        }

        viewModelScope.launch(exceptionHandler) {
            val tripCreator = memberInteractor.fetchTripMember(userUid = tripCreatorUid)

            if (tripCreator != null) {
                addTripMember(member = tripCreator)

                val calorieNorm = NutritionalCalculator.calculateTripCalorieNorm(
                    type = type,
                    difficultyCategory = difficultyCategory,
                    season = season,
                    dayQuantity = TripDateFormat.getDayQuantity(
                        startDate = startDate,
                        endDate = endDate
                    ),
                    members = addedMembers.value.toTypedArray()
                )

                val trip = Trip(
                    id = UUID.randomUUID().toString(),
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
}