package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.ProvisionBag
import com.example.hikeculator.domain.repositories.ProvisionBagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProvisionBagInteractor(private val provisionBagRepository: ProvisionBagRepository) {

    fun insertProductToProvisionBag(product: Product) =
        provisionBagRepository.insertProductToProvisionBag(product)

    fun fetchProvisionBag(): ProvisionBag = provisionBagRepository.fetchProvisionBag()

    suspend fun createProvisionBag(tripId: String, provisionBag: ProvisionBag) {
        withContext(Dispatchers.IO) {
            provisionBagRepository.createProvisionBag(tripId = tripId, provisionBag = provisionBag)
        }
    }

    suspend fun removeProvisionBag(tripId: String) {
        withContext(Dispatchers.IO) { provisionBagRepository.removeProvisionBag(tripId = tripId) }
    }
}