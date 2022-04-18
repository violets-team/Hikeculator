package com.example.hikeculator.presentation.product_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.domain.common.NutritionalCalculator
import com.example.hikeculator.domain.common.percentageOf
import com.example.hikeculator.domain.enums.MealType
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.interactors.DayMealInteractor
import com.example.hikeculator.domain.interactors.ProductInteractor
import com.example.hikeculator.domain.interactors.ProductSearchInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.repositories.SelectedProductRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductSearchViewModel(
    private val searchInteractor: ProductSearchInteractor,
    private val selectedProductRepository: SelectedProductRepository,
    private val mealType: MealType,
    dayMealInteractor: DayMealInteractor,
    tripInteractor: TripInteractor,
    tripId: String,
    dayId: String
) : ViewModel() {

    private val _productSearchResult = MutableSharedFlow<List<Product>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val productSearchResult = _productSearchResult.asSharedFlow()

    private val _autoCompleteList = MutableSharedFlow<List<String>>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val autoCompleteList = _autoCompleteList.asSharedFlow()

    private val _productStatistics = MutableSharedFlow<ProductStatistics>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val productStatistics = _productStatistics.asSharedFlow()

    private val dayMeal = dayMealInteractor.fetchDayMeal(
        tripId = tripId,
        dayId = dayId,
        mealType = mealType
    ).shareIn(scope = viewModelScope, started = SharingStarted.Lazily, replay = 1)

    private val trip = tripInteractor.fetchTrip(tripId = tripId).shareIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        replay = 1
    )

    private val _errors = MutableSharedFlow<Int>(replay = 1)
    val errors = _errors.asSharedFlow()

    init {
        viewModelScope.launch { collectDayMealAfterGettingTrip() }
    }

    fun autoComplete(searchExpression: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            viewModelScope.launch { _errors.tryEmit(R.string.text_error_search) }
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _autoCompleteList.tryEmit(searchInteractor.fetchHints(searchExpression = searchExpression))
        }
    }

    fun search(searchExpression: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            viewModelScope.launch { _errors.tryEmit(R.string.text_error_search) }
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _productSearchResult.emit(searchInteractor.search(searchExpression))
        }
    }

    fun saveSelectedProduct(product: Product) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            viewModelScope.launch { _errors.tryEmit(R.string.text_error_saving_selected_product) }
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            selectedProductRepository.saveProduct(product)
        }
    }

    private suspend fun collectDayMealAfterGettingTrip() {
        trip.collect { trip ->
            trip?.let { collectDayMeal(it) }
        }
    }

    private suspend fun collectDayMeal(trip: Trip) {
        val mealCaloriesNorm = NutritionalCalculator.getMealCaloriesNorm(
            trip = trip,
            mealType = mealType
        )
        val proteinsNorm = NutritionalCalculator.getProteinsNormInGrams(calories = mealCaloriesNorm)
        val fatsNorm = NutritionalCalculator.getFatNormInGrams(calories = mealCaloriesNorm)
        val carbsNorm = NutritionalCalculator.getCarbsNormInGrams(calories = mealCaloriesNorm)

        dayMeal.collect { dayMeal ->
            val statistic = ProductStatistics(
                percentageOfCalories = dayMeal.totalCalories percentageOf mealCaloriesNorm,
                percentageOfProteins = dayMeal.totalProteins percentageOf proteinsNorm,
                percentageOfFats = dayMeal.totalFats percentageOf fatsNorm,
                percentageOfCarbs = dayMeal.totalCarbs percentageOf carbsNorm
            )

            _productStatistics.tryEmit(statistic)
        }
    }
}