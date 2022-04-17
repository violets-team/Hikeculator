package com.example.hikeculator.presentation.trip_day_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.entities.TripDay
import com.example.hikeculator.domain.enums.MealType
import com.example.hikeculator.domain.interactors.ProductInteractor
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TripDayDetailViewModel(
    private val tripId: String,
    private val tripDayId: String,
    tripInteractor: TripInteractor,
    private val tripDayInteractor: TripDayInteractor,
    private val productInteractor: ProductInteractor,
) : ViewModel() {

    private val _problemMessage = MutableSharedFlow<Int>()
    val problemMessage = _problemMessage.asSharedFlow()

    private val trip = tripInteractor.fetchTrip(tripId = tripId)
    private val tripDay = getTripDayFlow(tripDayId = tripDayId)

    val tripData: SharedFlow<Pair<Trip?, TripDay?>> = combine(
        trip,
        tripDay
    ) { trip: Trip?, tripDay: TripDay? -> trip to tripDay }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )

    fun deleteFood(food: Product, mealType: MealType) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _problemMessage.tryEmit(R.string.problem_with_deleting_product)
        }

        if (tripData.replayCache.isNotEmpty()) {
            val tripDay = tripData.replayCache.first().second

            if (tripDay != null) {
                viewModelScope.launch(context = exceptionHandler) {
                    productInteractor.deleteProduct(
                        product = food,
                        tripId = tripId,
                        dayId = tripDayId,
                        mealType = mealType
                    )
                }
            }
        }
    }

    private fun getTripDayFlow(tripDayId: String): Flow<TripDay?> {
        return tripDayInteractor.fetchTripDay(tripId = tripId, tripDayId = tripDayId).catch {
            _problemMessage.tryEmit(R.string.problem_with_getting_trip_day)
        }
    }
}