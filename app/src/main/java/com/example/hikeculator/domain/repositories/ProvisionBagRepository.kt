package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.ProvisionBag
import kotlinx.coroutines.flow.Flow

interface ProvisionBagRepository {

    suspend fun insertProductToProvisionBag(tripId: String, product: Product)

    fun fetchProvisionBag(tripId: String): Flow<ProvisionBag>

    suspend fun createProvisionBag(tripId: String, provisionBag: ProvisionBag)

    suspend fun removeProvisionBag(tripId: String)
}