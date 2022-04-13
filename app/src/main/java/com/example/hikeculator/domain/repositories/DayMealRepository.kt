package com.example.hikeculator.domain.repositories

import com.example.hikeculator.data.fiebase.entities.FirestoreProduct
import com.example.hikeculator.data.fiebase.entities.FirestoreTripDay
import com.example.hikeculator.domain.entities.DayMeal
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.enums.MealType
import kotlinx.coroutines.flow.Flow

interface DayMealRepository {

    fun fetchDayMeal(tripId: String, dayId: String, mealType: MealType): Flow<DayMeal>

    suspend fun addProductToDayMeal(
        tripId: String,
        firestoreTripDay: FirestoreTripDay,
        firestoreProduct: FirestoreProduct,
        mealType: MealType
    )

    suspend fun removeProductFromDayMeal(
        tripId: String,
        firestoreTripDay: FirestoreTripDay,
        firestoreProduct: FirestoreProduct,
        mealType: MealType
    )
}