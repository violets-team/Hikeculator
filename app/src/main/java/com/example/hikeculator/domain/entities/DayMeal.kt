package com.example.hikeculator.domain.entities

data class DayMeal(
    val products: List<Product>,
) {
    val totalCalories: Double
        get() = products.sumOf { product -> product.nutritionalValue.calories * product.weight }

    val totalWeight: Long
        get() = products.sumOf { product -> product.weight }

    val totalProteins: Double
        get() = products.sumOf { product -> product.nutritionalValue.proteins * product.weight }

    val totalFats: Double
        get() = products.sumOf { product -> product.nutritionalValue.fats * product.weight }

    val totalCarbs: Double
        get() = products.sumOf { product -> product.nutritionalValue.carbs * product.weight }
}