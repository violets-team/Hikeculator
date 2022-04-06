package com.example.hikeculator.presentation.product_dialogs.add_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.domain.entities.NutritionalValue
import com.example.hikeculator.domain.enums.MealType
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.interactors.ProductInteractor
import com.example.hikeculator.domain.repositories.SelectedProductRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class AddOrEditProductDialogViewModel(
    private val selectedProductRepository: SelectedProductRepository,
    private val productInteractor: ProductInteractor
) : ViewModel() {

    private val _selectedProduct = MutableSharedFlow<Product>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val selectedProduct = _selectedProduct.asSharedFlow()

    private val _displayedNutritionalValue = MutableSharedFlow<NutritionalValue>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val displayedNutritionalValue = _displayedNutritionalValue.asSharedFlow()

    private val _errors = MutableSharedFlow<Int>(replay = 1)
    val errors = _errors.asSharedFlow()

    init {
        fetchSelectedProduct()
    }

    fun setWeight(weight: Long) {
        val productPerUnitOfMeasure = _selectedProduct.replayCache.first()
        _displayedNutritionalValue.tryEmit(
            productPerUnitOfMeasure.nutritionalValue.getCalculatedNutritionalValue(weight)
        )
    }

    fun addProductToTrip(tripId: String, dayId: String, mealType: MealType, weight: Long) {
        viewModelScope.launch {
            selectedProduct.replayCache.firstOrNull()?.let { product ->
                productInteractor.insertProduct(
                    tripId = tripId,
                    dayId = dayId,
                    product = product.copy(weight = weight),
                    mealType = mealType
                )
            }
        }
    }

    private fun fetchSelectedProduct() {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _errors.tryEmit(R.string.text_error_saving_receiving_product)
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val product = selectedProductRepository.fetchProduct()
            _selectedProduct.emit(product)
        }
    }
}