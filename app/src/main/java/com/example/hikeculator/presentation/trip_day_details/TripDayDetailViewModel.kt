package com.example.hikeculator.presentation.trip_day_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.data.repository_implementations.TripDayRepositoryImpl
import com.example.hikeculator.domain.entities.DayMeal
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.repositories.TripDayRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class TripDayDetailViewModel(
    userTripCollectionId: String,
    tripId: String,
    tripDayId: String,
) : ViewModel() {

    private val tripDayRepository: TripDayRepository = TripDayRepositoryImpl(
        userTripCollectionId = userTripCollectionId,
        tripId = tripId
    )
    private val tripDayInteractor = TripDayInteractor(tripDayRepository = tripDayRepository)

    val tripDayData = getTripDayFlow(tripDayId = tripDayId).shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )

    fun updateTripDay(
        tripDayId: String,
        breakfastProducts: List<Product>,
        lunchProducts: List<Product>,
        dinnerProducts: List<Product>,
        snackProducts: List<Product>,
    ) {
        val updatedTripDay = TripDay(
            id = tripDayId,
            breakfast = DayMeal(products = breakfastProducts),
            lunch = DayMeal(products = lunchProducts),
            dinner = DayMeal(products = dinnerProducts),
            snack = DayMeal(products = snackProducts)
        )

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            TODO("Handle the exception here")
        }

        viewModelScope.launch(context = exceptionHandler) {
            tripDayInteractor.insertTripDay(tripDay = updatedTripDay)
        }
    }

    private fun getTripDayFlow(tripDayId: String): Flow<TripDay?> {
        return tripDayInteractor.fetchTripDay(tripDayId = tripDayId).catch {
            TODO("Handel the excepting here")
        }
    }
}