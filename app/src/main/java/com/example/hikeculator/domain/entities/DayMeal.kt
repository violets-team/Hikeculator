package com.example.hikeculator.domain.entities

data class DayMeal(
    val products: List<Product>,
) {
    val totalCalories: Double
        get() = products.map { product -> product.nutritionalValue.calories }.sum()

    val totalWeight: Long
        get() = products.map { product -> product.weight }.sum()

    val totalProteins: Double
        get() = products.map { product -> product.nutritionalValue.proteins }.sum()

    val totalFats: Double
        get() = products.map { product -> product.nutritionalValue.fats }.sum()

    val totalCarbs: Double
        get() = products.map { product -> product.nutritionalValue.carbs }.sum()
}