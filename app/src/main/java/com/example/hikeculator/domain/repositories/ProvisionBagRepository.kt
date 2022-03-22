package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.ProvisionBag

interface ProvisionBagRepository {

    fun insertProductToProvisionBag(product: Product)

    fun fetchProvisionBag(): ProvisionBag

    suspend fun createProvisionBag(tripId: String, provisionBag: ProvisionBag)

    suspend fun removeProvisionBag(tripId: String)
}