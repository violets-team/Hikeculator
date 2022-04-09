package com.example.hikeculator.domain.entities

import com.example.hikeculator.domain.common.VALUE_ONE_HUNDRED


data class NutritionalValue(
    val calories: Double,
    val proteins: Double,
    val fats: Double,
    val carbs: Double,
) {
    fun mapToNutritionHundredGrams() = NutritionalValue(
        calories = nutritionValueForHundredGrams(calories),
        proteins = nutritionValueForHundredGrams(proteins),
        fats = nutritionValueForHundredGrams(fats),
        carbs = nutritionValueForHundredGrams(carbs)
    )

    private fun nutritionValueForHundredGrams(value: Double): Double = VALUE_ONE_HUNDRED * value

    fun getCalculatedNutritionalValue(weight: Long): NutritionalValue = copy(
        calories = calories * weight,
        proteins = proteins * weight,
        fats = fats * weight,
        carbs = carbs * weight
    )
}