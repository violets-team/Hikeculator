package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.enums.MealType
import com.example.hikeculator.domain.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ProductInteractor(private val productRepository: ProductRepository) {

    suspend fun insertProduct(
        product: Product,
        tripId: String,
        dayId: String,
        mealType: MealType
    ) = withContext(Dispatchers.IO) {
        productRepository.insertProduct(
            product = product,
            tripId = tripId,
            dayId = dayId,
            mealType = mealType
        )
    }

    suspend fun deleteProduct(
        product: Product,
        tripId: String,
        dayId: String,
        mealType: MealType
    ) {
        productRepository.removeProduct(
            product = product,
            tripId = tripId,
            dayId = dayId,
            mealType = mealType
        )
    }
}