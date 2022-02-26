package com.example.hikeculator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.data.repository_implementations.TripRepositoryImpl
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.enums.Seasons
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripType
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.repositories.TripRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GeneralTripViewModel(userTripCollectionId: String) : ViewModel() {

    private val tripRepository: TripRepository =
        TripRepositoryImpl(tripCollectionId = userTripCollectionId)
    private val tripInteractor = TripInteractor(tripRepository)


    val tripData: StateFlow<Set<Trip>> = getTripFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptySet()
    )

    fun createTrip(
        id: String,
        name: String,
        startDate: Long,
        endDate: Long,
        memberCount: Int,
        totalCalories: Double,
        type: TripType,
        difficultyCategory: TripDifficultyCategory,
        season: Seasons,
    ) {
        val trip = Trip(
            id = id,
            name = name,
            startDate = startDate,
            endDate = endDate,
            memberCount = memberCount,
            totalCalories = totalCalories,
            type = type,
            difficultyCategory = difficultyCategory,
            season = season
        )

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            TODO("Handle the exception here")
        }

        viewModelScope.launch(exceptionHandler) { tripInteractor.insertTrip(trip = trip) }
    }

    fun deleteTrip(tripId: String) {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            TODO("Handle the exception here")
        }

        viewModelScope.launch(exceptionHandler) { tripInteractor.removeTrip(tripId = tripId) }
    }

    private fun getTripFlow(): Flow<Set<Trip>> = tripInteractor.fetchTrips().catch { throwable ->
        TODO("Handle the exception here")
    }
}