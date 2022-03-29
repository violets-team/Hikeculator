package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.ProvisionBag
import com.example.hikeculator.domain.repositories.ProvisionBagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProvisionBagInteractor(private val provisionBagRepository: ProvisionBagRepository) {

    suspend fun insertProduct(tripId: String, product: Product) =
        provisionBagRepository.insertProductToProvisionBag(tripId = tripId, product = product)

    fun fetchProvisionBag(tripId: String): Flow<ProvisionBag> {
        return provisionBagRepository.fetchProvisionBag(tripId = tripId)
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