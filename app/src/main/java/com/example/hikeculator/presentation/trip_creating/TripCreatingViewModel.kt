package com.example.hikeculator.presentation.trip_creating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripSeason
import com.example.hikeculator.domain.enums.TripType
import com.example.hikeculator.domain.interactors.MemberGroupInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class TripCreatingViewModel(
    private val tripInteractor: TripInteractor,
    private val memberInteractor: MemberGroupInteractor,
) : ViewModel() {

    private val _season = MutableStateFlow<TripSeason?>(null)
    val season = _season.asStateFlow()

    private val _category = MutableStateFlow<TripDifficultyCategory?>(null)
    val category = _category.asStateFlow()

    private val _type = MutableStateFlow<TripType?>(null)
    val type = _type.asStateFlow()

    private val _date = MutableStateFlow<Pair<Long, Long>?>(null)
    val date = _date.asStateFlow()

    private val _addedMembers = MutableStateFlow<Set<User>>(emptySet())
    val addedMembers = _addedMembers.asStateFlow()

    private val _searchedMemberFlow = MutableStateFlow<Set<User>>(emptySet())
    val searchedMembers = _searchedMemberFlow.asStateFlow()

    fun setSeason(season: TripSeason?) {
        _season.value = season
    }

    fun setCategory(category: TripDifficultyCategory) {
        _category.value = category
    }

    fun setType(type: TripType) {
        _type.value = type
    }

    fun setDate(pickedDate: Pair<Long, Long>) {
        _date.value = pickedDate
    }

    fun createTrip(
        name: String,
        startDate: Long,
        endDate: Long,
        type: TripType,
        difficultyCategory: TripDifficultyCategory,
        season: TripSeason,
    ) {

        val trip = Trip(
            id = UUID.randomUUID().toString(),
            name = name,
            startDate = startDate,
            endDate = endDate,
            memberUids = _addedMembers.value.map { member -> member.uid }.toSet(),
            totalCalories = 124132.14,
            type = type,
            difficultyCategory = difficultyCategory,
            season = season
        )

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            TODO("Handle the exception here")
        }

        viewModelScope.launch(exceptionHandler) { tripInteractor.insertTrip(trip = trip) }

        TODO("Implement calorie calculating")
    }

    fun addTripMember(member: User) {
        val updatedMemberCollection = mutableSetOf<User>().apply {
            addAll(_addedMembers.value)
            add(member)
        }
        _addedMembers.value = updatedMemberCollection
    }

    fun removeTripMember(member: User) {
        val updatedMemberCollection = mutableSetOf<User>().apply {
            addAll(_addedMembers.value)
            remove(member)
        }
        _addedMembers.value = updatedMemberCollection
    }

    fun clearSearchedMemberList() {
        _searchedMemberFlow.value = emptySet()
    }

    fun searchByEmail(text: String) {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            TODO()
        }

        viewModelScope.launch(exceptionHandler) {
            _searchedMemberFlow.value = memberInteractor.searchTripMembers(email = text)
        }
    }
}