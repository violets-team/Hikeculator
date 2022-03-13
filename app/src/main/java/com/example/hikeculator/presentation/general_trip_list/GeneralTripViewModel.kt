package com.example.hikeculator.presentation.general_trip_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.interactors.TripInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GeneralTripViewModel(private val tripInteractor: TripInteractor) : ViewModel() {

    val tripData: SharedFlow<Set<Trip>> = getTripFlow().shareIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        replay = 1
    )

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