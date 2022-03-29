package com.example.hikeculator.presentation.provision_bag

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.domain.entities.NutritionalValue
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.ProvisionBag
import com.example.hikeculator.domain.interactors.ProvisionBagInteractor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProvisionBagViewModel(
    private val tripId: String,
    private val provisionBagInteractor: ProvisionBagInteractor,
) : ViewModel() {

    private val provisionBag = getProvisionBag().shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )

    val products = provisionBag.map { provisionBag -> provisionBag.productList }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )

    private fun getProvisionBag(): Flow<ProvisionBag> {
        return provisionBagInteractor.fetchProvisionBag(tripId = tripId)
    }

    fun insertProduct() {
        viewModelScope.launch {

            Log.i("app_log", "insert: **********************")
            provisionBagInteractor.insertProduct(
                tripId = tripId,
                product = Product(
                    name = "Product ${(0..1111111).random()}",
                    weight = 255,
                    nutritionalValue = NutritionalValue(1000, 10, 10, 10)
                )
            )
        }
    }
}