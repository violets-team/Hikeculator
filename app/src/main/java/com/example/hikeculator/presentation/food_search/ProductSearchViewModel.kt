package com.example.hikeculator.presentation.food_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.data.repository_implementations.ProductSearchRepositoryImpl
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.interactors.ProductSearchInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ProductSearchViewModel() : ViewModel() {

    private val _productSearchResult = MutableSharedFlow<List<Product>>()
    val productSearchResult = _productSearchResult.asSharedFlow()

    private val _searchError = MutableSharedFlow<Int>()
    val searchError = _searchError.asSharedFlow()

    private val searchInteractor: ProductSearchInteractor =
        ProductSearchInteractor(ProductSearchRepositoryImpl())

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        viewModelScope.launch { _searchError.emit(R.string.text_error_search) }
    }

    fun search(searchExpression: String) =
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _productSearchResult.emit(searchInteractor.search(searchExpression))
        }
}