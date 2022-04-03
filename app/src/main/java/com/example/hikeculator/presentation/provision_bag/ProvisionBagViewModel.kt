package com.example.hikeculator.presentation.provision_bag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.domain.entities.ProvisionBag
import com.example.hikeculator.domain.entities.ProvisionBagProduct
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.interactors.ProvisionBagInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProvisionBagViewModel(
    private val tripId: String,
    private val provisionBagInteractor: ProvisionBagInteractor,
    private val tripInteractor: TripInteractor
) : ViewModel() {

    val products = getProvisionBag().map { provisionBag -> provisionBag.productList }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )

    val tripName = getObservableTrip().mapNotNull { trip: Trip? -> trip?.name }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )

    private val _problemMessage: MutableSharedFlow<Int> = MutableSharedFlow()
    val problemMessage: SharedFlow<Int> = _problemMessage.asSharedFlow()

    fun updatedProduct(updatedProduct: ProvisionBagProduct) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _problemMessage.tryEmit(R.string.problem_with_product_updating)
        }

        viewModelScope.launch(context = exceptionHandler) {
            provisionBagInteractor.updateProduct(tripId = tripId, updatedProduct = updatedProduct)
        }
    }

    private fun getProvisionBag(): Flow<ProvisionBag> {
        return provisionBagInteractor.fetchProvisionBag(tripId = tripId)
    }

    private fun getObservableTrip(): Flow<Trip?> {
        return tripInteractor.fetchTrip(tripId = tripId)
    }
}