package com.example.hikeculator.presentation.product_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.interactors.ProductSearchInteractor
import com.example.hikeculator.domain.repositories.SelectedProductRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ProductSearchViewModel(
    private val searchInteractor: ProductSearchInteractor,
    private val selectedProductRepository: SelectedProductRepository
) : ViewModel() {

    private val _productSearchResult = MutableSharedFlow<List<Product>>(
        replay = 0,
        extraBufferCapacity = 1,
        BufferOverflow.DROP_OLDEST
    )
    val productSearchResult = _productSearchResult.asSharedFlow()

    private val _searchError = MutableSharedFlow<Int>()
    val searchError = _searchError.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch { _searchError.emit(R.string.text_error_search) }
    }

    fun search(searchExpression: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _productSearchResult.emit(searchInteractor.search(searchExpression))
        }
    }

    fun saveSelectedProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedProductRepository.saveProduct(product)
        }
    }
}