package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.ProvisionBag

interface ProvisionBagRepository {

    fun insertProductToProvisionBag(product: Product)

    fun fetchProvisionBag(): ProvisionBag

    fun fetchProvisionBagDetails()
}