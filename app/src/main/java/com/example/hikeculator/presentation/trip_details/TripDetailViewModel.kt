package com.example.hikeculator.presentation.trip_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    tripInteractor: TripInteractor,
    private val tripDayInteractor: TripDayInteractor,
    private val tripId: String,
) : ViewModel() {

    val tripDays: SharedFlow<List<TripDay>> = getTripDayFlow().shareIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        replay = 1
    )

    val trip: SharedFlow<Trip?> = tripInteractor.fetchTrip(tripId = tripId).shareIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        replay = 1
    )

    fun addTripDay(
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

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            TODO("Handle the exception here")
        }

        viewModelScope.launch(context = exceptionHandler) {
            tripDayInteractor.insertTripDay(tripId = tripId, tripDay = tripDay)
        }
    }

    private fun getTripDayFlow(): Flow<List<TripDay>> {
        return tripDayInteractor.fetchTripDays(tripId = tripId).catch {
            TODO("Handle the exception here")
        }
    }
}
