package com.example.hikeculator.presentation.trip_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import kotlinx.coroutines.flow.*

class TripDetailViewModel(
    private val tripInteractor: TripInteractor,
    private val tripDayInteractor: TripDayInteractor,
    private val tripId: String,
) : ViewModel() {

    private val _problemMessage = MutableSharedFlow<Int>(replay = 1, extraBufferCapacity = 1)
    val problemMessage: SharedFlow<Int> = _problemMessage.asSharedFlow()

    val data: SharedFlow<Pair<Trip?, List<TripDay>>> = getTripData().shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )


    private fun getTripData(): Flow<Pair<Trip?, List<TripDay>>> {
        return combine(
            getTrip(),
            getTripDays()
        ) { trip: Trip?, tripDays -> trip to tripDays }
    }

    private fun getTripDays(): Flow<List<TripDay>> {
        return tripDayInteractor.fetchTripDays(tripId = tripId).catch {
            _problemMessage.tryEmit(R.string.problem_with_trip_getting)
        }
    }

    private fun getTrip(): Flow<Trip?> {
        return tripInteractor.fetchTrip(tripId = tripId).catch {
            _problemMessage.emit(R.string.problem_with_trip_getting)
        }
    }
}
