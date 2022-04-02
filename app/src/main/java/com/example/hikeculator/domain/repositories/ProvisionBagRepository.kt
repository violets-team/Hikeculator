package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.ProvisionBag
import com.example.hikeculator.domain.entities.ProvisionBagProduct
import kotlinx.coroutines.flow.Flow

interface ProvisionBagRepository {

    suspend fun insertProductToProvisionBag(tripId: String, product: ProvisionBagProduct)

    suspend fun updateProduct(tripId: String, updatedProduct: ProvisionBagProduct)

    fun fetchObservableProvisionBag(tripId: String): Flow<ProvisionBag>

    suspend fun createProvisionBag(tripId: String, provisionBag: ProvisionBag)

    suspend fun removeProvisionBag(tripId: String)
}