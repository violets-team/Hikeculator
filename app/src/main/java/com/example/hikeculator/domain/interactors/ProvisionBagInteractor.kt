package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.entities.ProvisionBag
import com.example.hikeculator.domain.repositories.ProvisionBagRepository

class ProvisionBagInteractor(private val provisionBagRepository: ProvisionBagRepository) {

    fun insertProductToProvisionBag(product: Product) {
        provisionBagRepository.insertProductToProvisionBag(product)
    }

    fun fetchProvisionBag(): ProvisionBag {
        return provisionBagRepository.fetchProvisionBag()
    }

    fun fetchProvisionBagDetails() {
        return provisionBagRepository.fetchProvisionBagDetails()
    }
}