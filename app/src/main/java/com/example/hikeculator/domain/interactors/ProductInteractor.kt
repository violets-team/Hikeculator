package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.DayMeal
import com.example.hikeculator.domain.enums.MealType
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    fun fetchDayMeal(tripId: String, dayId: String, mealType: MealType): Flow<DayMeal> {
        return productRepository.fetchDayMeal(
            tripId = tripId,
            dayId = dayId,
            mealType = mealType
        )
    }

}