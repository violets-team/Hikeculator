package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.MealType
import com.example.hikeculator.domain.entities.Product

interface ProductRepository {

    suspend fun insertProduct(product: Product,tripId: String, dayId: String, mealType: MealType)
}