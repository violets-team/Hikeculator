package com.example.hikeculator.presentation.trip_creating

import androidx.lifecycle.ViewModel
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripSeason
import com.example.hikeculator.domain.enums.TripType
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class ITripCreatingViewModel : ViewModel() {

    abstract val seasonState: StateFlow<TripSeason?>
    abstract val categoryState: StateFlow<TripDifficultyCategory?>
    abstract val typeState: StateFlow<TripType?>
    abstract val dateState: StateFlow<Pair<Long, Long>?>
    abstract val addedMembers: StateFlow<Set<User>>
    abstract val searchedMembers: StateFlow<Set<User>>
    abstract val problemMessage: SharedFlow<Int>

    abstract fun setSeason(season: TripSeason?)

    abstract fun setCategory(category: TripDifficultyCategory)

    abstract fun setType(type: TripType)

    abstract fun setDate(pickedDate: Pair<Long, Long>)

    abstract fun addTripMember(member: User)

    abstract fun removeTripMember(member: User)

    abstract fun clearSearchedMemberList()

    abstract fun searchByEmail(text: String)

    abstract fun createTrip(
        name: String,
        startDate: Long,
        endDate: Long,
        type: TripType,
        difficultyCategory: TripDifficultyCategory,
        season: TripSeason,
    )

    protected abstract fun clearTripCreatingState()

    protected abstract fun createEmptyTripDays(tripId: String, startDate: Long, endDate: Long)
}