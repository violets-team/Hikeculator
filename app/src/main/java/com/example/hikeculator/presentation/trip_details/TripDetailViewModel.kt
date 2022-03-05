package com.example.hikeculator.presentation.trip_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.data.repository_implementations.TripDayRepositoryImpl
import com.example.hikeculator.domain.entities.DayMeal
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.repositories.TripDayRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class TripDetailViewModel(
    userTripCollectionId: String,
    tripId: String,
) : ViewModel() {

    private val tripDayRepository: TripDayRepository = TripDayRepositoryImpl(
        userTripCollectionId = userTripCollectionId,
        tripId = tripId
    )

    private val tripDayInteractor = TripDayInteractor(tripDayRepository = tripDayRepository)

    val tripDays: SharedFlow<List<TripDay>> = getTripDayFlow().shareIn(
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
            breakfast = DayMeal(products = breakfastProducts),
            lunch = DayMeal(products = lunchProducts),
            dinner = DayMeal(products = dinnerProducts),
            snack = DayMeal(products = snackProducts)
        )

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            TODO("Handle the exception here")
        }

        viewModelScope.launch(context = exceptionHandler) {
            tripDayInteractor.insertTripDay(tripDay = tripDay)
        }
    }

    private fun getTripDayFlow(): Flow<List<TripDay>> = tripDayInteractor.fetchTripDays().catch {
        TODO("Handle the exception here")
    }
}
