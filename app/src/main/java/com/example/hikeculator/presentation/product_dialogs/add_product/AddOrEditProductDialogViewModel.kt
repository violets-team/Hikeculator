package com.example.hikeculator.presentation.product_dialogs.add_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.interactors.ProvisionBagInteractor
import com.example.hikeculator.domain.repositories.SelectedProductRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class AddOrEditProductDialogViewModel(
    private val selectedProductRepository: SelectedProductRepository,
    private val provisionBagInteractor: ProvisionBagInteractor
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
        _displayedProduct.tryEmit(productPerUnitOfMeasure.getNewProductByWeight(weight))
    }

    fun addProductToProvisionBag() {

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