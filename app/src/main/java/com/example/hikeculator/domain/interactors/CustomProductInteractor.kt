package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.repositories.CustomProductRepository

class CustomProductInteractor(private val customProductRepository: CustomProductRepository) {

    fun insertCustomProductToDatabase() = customProductRepository.insertCustomProductToDatabase()
}