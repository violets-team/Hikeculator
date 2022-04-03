package com.example.hikeculator.presentation.product_dialogs.add_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.domain.entities.MealType
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

    private val _displayedProduct = MutableSharedFlow<Product>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val displayedProduct = _displayedProduct.asSharedFlow()

    init {
        fetchSelectedProduct()
    }

    fun setWeight(weight: Long) {
        val productPerUnitOfMeasure = _selectedProduct.replayCache.first()
        _displayedProduct.tryEmit(productPerUnitOfMeasure.getProductWithCalculatedNutritionalValue(weight))
    }

    fun addProductToTrip(tripId: String, dayId: String, mealType: MealType, weight: Long) {
        viewModelScope.launch {
            val product = selectedProduct.replayCache.firstOrNull()
            product?.copy(weight = weight)?.let {
                productInteractor.insertProduct(
                    tripId = tripId,
                    dayId = dayId,
                    product = it,
                    mealType = mealType
                )
            }
        }
    }

    private fun fetchSelectedProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            val product: Deferred<Product> = async {
                selectedProductRepository.fetchProduct()
            }
            _selectedProduct.emit(product.await())
        }
    }
}