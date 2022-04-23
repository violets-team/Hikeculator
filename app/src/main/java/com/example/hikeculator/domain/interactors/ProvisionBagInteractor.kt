package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.ProvisionBag
import com.example.hikeculator.domain.entities.ProvisionBagProduct
import com.example.hikeculator.domain.repositories.ProvisionBagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProvisionBagInteractor(private val provisionBagRepository: ProvisionBagRepository) {

    suspend fun insertProduct(tripId: String, product: ProvisionBagProduct) {
        withContext(Dispatchers.IO) {
            provisionBagRepository.insertProductToProvisionBag(tripId = tripId, product = product)
        }
    }

    suspend fun updateProduct(tripId: String ,updatedProduct: ProvisionBagProduct) {
        withContext(Dispatchers.IO) {
            provisionBagRepository.updateProduct(tripId = tripId, updatedProduct = updatedProduct)
        }
    }

    fun fetchProvisionBag(tripId: String): Flow<ProvisionBag> {
        return provisionBagRepository.fetchObservableProvisionBag(tripId = tripId)
    }

    suspend fun createProvisionBag(tripId: String, provisionBag: ProvisionBag) {
        withContext(Dispatchers.IO) {
            provisionBagRepository.createProvisionBag(tripId = tripId, provisionBag = provisionBag)
        }
    }

    suspend fun removeProvisionBag(tripId: String) {
        withContext(Dispatchers.IO) { provisionBagRepository.removeProvisionBag(tripId = tripId) }
    }
}