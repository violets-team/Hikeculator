package com.example.hikeculator.domain.entities

data class DayMeal(
    val products: List<Product>,
) {

    val totalCalories: Double
        get() = products.map { it.nutritionalValue.calories }.sum()

    val totalWeight: Double
        get() = products.map { it.weight }.sum()
}