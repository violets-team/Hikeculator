package com.example.hikeculator.presentation.general_trip_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.interactors.ProvisionBagInteractor
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.interactors.UserProfileInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GeneralTripViewModel(
    private val tripInteractor: TripInteractor,
    private val tripDayInteractor: TripDayInteractor,
    private val provisionBagInteractor: ProvisionBagInteractor,
    userProfileInteractor: UserProfileInteractor
) : ViewModel() {

    private val _trips = MutableSharedFlow<Set<Trip>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val trips = _trips

    private val _problemMessage = MutableSharedFlow<Int>()
    val problemMessage = _problemMessage.asSharedFlow()

    private val user = userProfileInteractor.fetchObservableUserProfile().shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )

    init {
        observeUserProfile()
    }

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
            user.onEach { user: User? ->
                user?.let { fetchTrips(tripId = user.tripIds.toTypedArray()) }
            }.collect()
        }
    }

    private fun fetchTrips(vararg tripId: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _problemMessage.tryEmit(R.string.porblem_with_trip_collection_getting)
        }

        viewModelScope.launch(context = exceptionHandler) {
            val trips = tripInteractor.fetchTrips(tripId = tripId)
            _trips.emit(value = trips)
        }
    }
}