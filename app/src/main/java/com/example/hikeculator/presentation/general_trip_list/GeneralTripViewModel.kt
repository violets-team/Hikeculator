package com.example.hikeculator.presentation.general_trip_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GeneralTripViewModel(
    private val tripInteractor: TripInteractor,
    private val tripDayInteractor: TripDayInteractor,
) : ViewModel() {

    val tripData: SharedFlow<Set<Trip>> = fetchTrip().shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )

    private val _problemMessage = MutableSharedFlow<Int>()
    val problemMessage = _problemMessage.asSharedFlow()

    fun deleteTrip(tripId: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _problemMessage.tryEmit(R.string.problem_with_trip_deleting)
        }

        viewModelScope.launch(exceptionHandler) {
            launch { tripDayInteractor.removeTripDayCollection(tripId = tripId) }
            launch { tripInteractor.removeTrip(tripId = tripId) }
        }
    }

    private fun fetchTrip(): Flow<Set<Trip>> = tripInteractor.fetchTrips().catch { _ ->
        _problemMessage.tryEmit(R.string.problem_with_trip_getting)
    }
}