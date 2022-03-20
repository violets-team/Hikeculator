package com.example.hikeculator.presentation.trip_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.domain.entities.DayMeal
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class TripDetailViewModel(
    private val tripInteractor: TripInteractor,
    private val tripDayInteractor: TripDayInteractor,
    private val tripId: String,
) : ViewModel() {

    val data: SharedFlow<Pair<Trip?, List<TripDay>>> = getTripData().shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )

    private val _problemMessage = MutableSharedFlow<Int>()
    val problemMessage: SharedFlow<Int> = _problemMessage.asSharedFlow()

    fun updateTripDay(
        breakfastProducts: List<Product>,
        lunchProducts: List<Product>,
        dinnerProducts: List<Product>,
        snackProducts: List<Product>,
    ) {

        val tripDay = TripDay(
            id = UUID.randomUUID().toString(),
            date = System.currentTimeMillis(),
            breakfast = DayMeal(products = breakfastProducts),
            lunch = DayMeal(products = lunchProducts),
            dinner = DayMeal(products = dinnerProducts),
            snack = DayMeal(products = snackProducts)
        )

        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _problemMessage.tryEmit(R.string.problem_with_trip_updating)
        }

        viewModelScope.launch(context = exceptionHandler) {
            tripDayInteractor.insertTripDay(tripId = tripId, tripDay = tripDay)
        }
    }

    private fun getTripData(): Flow<Pair<Trip?, List<TripDay>>> {
        return combine(getTrip(), getTripDays()) { trip: Trip?, tripDays ->
            trip to tripDays
        }
    }

    private fun getTripDays(): Flow<List<TripDay>> {
        return tripDayInteractor.fetchTripDays(tripId = tripId).catch {
            _problemMessage.tryEmit(R.string.problem_with_trip_getting)
        }
    }

    private fun getTrip(): Flow<Trip?> = tripInteractor.fetchTrip(tripId = tripId).catch {
        _problemMessage.tryEmit(R.string.problem_with_trip_getting)
    }
}
