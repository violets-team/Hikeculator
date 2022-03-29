package com.example.hikeculator.presentation.general_trip_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.interactors.ProvisionBagInteractor
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.interactors.UserProfileInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GeneralTripViewModel(
    private val tripInteractor: TripInteractor,
    private val tripDayInteractor: TripDayInteractor,
    private val provisionBagInteractor: ProvisionBagInteractor,
    private val userProfileInteractor: UserProfileInteractor
) : ViewModel() {

    init {
       observeUserProfile()
    }

    private val _trips = MutableSharedFlow<Set<Trip>>()
    val trips = _trips

    private val _problemMessage = MutableSharedFlow<Int>()
    val problemMessage = _problemMessage.asSharedFlow()

    fun deleteTrip(trip: Trip) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _problemMessage.tryEmit(R.string.problem_with_trip_deleting)
        }

        viewModelScope.launch(exceptionHandler) {
            launch { tripDayInteractor.removeTripDayCollection(tripId = trip.id) }
            launch { provisionBagInteractor.removeProvisionBag(tripId = trip.id) }
            launch { tripInteractor.removeTrip(trip = trip) }
        }
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            userProfileInteractor.fetchObservableUserProfile().shareIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                replay = 1
            ).onEach { fetchTrips() }
                .collect()
        }
    }

    private fun fetchTrips() {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            TODO()
        }

        viewModelScope.launch(context = exceptionHandler) {
            _trips.emit(tripInteractor.fetchTrips())
        }
    }
}