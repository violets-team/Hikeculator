package com.example.hikeculator.domain.interactors

import com.example.hikeculator.domain.entities.DayMeal
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.enums.MealType
import com.example.hikeculator.domain.repositories.DayMealRepository
import kotlinx.coroutines.flow.Flow

class DayMealInteractor(private val dayMealRepository: DayMealRepository) {

    fun fetchDayMeal(tripId: String, dayId: String, mealType: MealType): Flow<DayMeal> {
        return dayMealRepository.fetchDayMeal(
            tripId = tripId,
            dayId = dayId,
            mealType = mealType
        )
    }
}