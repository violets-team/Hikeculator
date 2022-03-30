package com.example.hikeculator.presentation.product_dialogs.add_product

import androidx.lifecycle.ViewModel
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.repositories.SelectedProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AddOrEditProductDialogViewModel(
    private val selectedProductRepository: SelectedProductRepository
) : ViewModel() {

    private val _product = MutableSharedFlow<Product>()
    val product = _product.asSharedFlow()

    fun setProductValueByWeight(weight: Double) {
        TODO()
    }
}