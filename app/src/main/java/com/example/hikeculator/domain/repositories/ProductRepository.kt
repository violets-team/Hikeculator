package com.example.hikeculator.domain.repositories

import com.example.hikeculator.domain.entities.DayMeal
import com.example.hikeculator.domain.enums.MealType
import com.example.hikeculator.domain.entities.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun insertProduct(product: Product,tripId: String, dayId: String, mealType: MealType)

    suspend fun removeProduct(product: Product,tripId: String, dayId: String, mealType: MealType)
}