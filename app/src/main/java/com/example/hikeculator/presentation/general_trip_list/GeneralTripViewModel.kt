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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GeneralTripViewModel(
    private val tripInteractor: TripInteractor,
    private val tripDayInteractor: TripDayInteractor,
    private val provisionBagInteractor: ProvisionBagInteractor,
    userProfileInteractor: UserProfileInteractor
) : ViewModel() {

    private var tripCollectingJob: Job? = null

    private val _trips = MutableStateFlow<Set<Trip>>(value = emptySet())
    val trips = _trips.asStateFlow()

    private val _problemMessage = MutableSharedFlow<Int>()
    val problemMessage = _problemMessage.asSharedFlow()

    private val user = userProfileInteractor.fetchObservableUserProfile().shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )

    init {
        observeData()
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

    private fun observeData() {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _problemMessage.tryEmit(R.string.problem_with_trip_collection_getting)
        }

        viewModelScope.launch(context = exceptionHandler) {
            user.collect { user: User? ->
                user ?: return@collect

                _trips.value = emptySet()
                tripCollectingJob?.cancel()
                tripCollectingJob = launch { observeTrips(user = user) }
            }
        }
    }

    private suspend fun observeTrips(user: User) {
        fetchObservableTrips(
            tripIds = user.tripIds.toTypedArray()
        ).collect { fetchedTrips ->
            val updatedTrips = _trips.value.toMutableList()

            fetchedTrips.onEach { trip ->
                if (isTripUpdated(trip = trip)) {
                    val index = _trips.value.indexOfFirst { trip.id == it.id }

                    if (index != -1) {
                        updatedTrips[index] = trip
                    }

                } else if (isTripNew(trip = trip)) {
                    updatedTrips.add(trip)
                }
            }
            _trips.value = updatedTrips.toSet()
        }
    }

    private fun isTripUpdated(trip: Trip): Boolean {
        return !_trips.value.contains(trip) &&
                _trips.value.firstOrNull { trip.id == it.id } != null
    }

    private fun isTripNew(trip: Trip): Boolean {
        return !_trips.value.contains(trip) &&
                _trips.value.firstOrNull { trip.id == it.id } == null
    }

    private fun fetchObservableTrips(vararg tripIds: String): Flow<Set<Trip>> {
        return tripInteractor.fetchObservableTrips(tripIds = tripIds).shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 0
        )
    }
}