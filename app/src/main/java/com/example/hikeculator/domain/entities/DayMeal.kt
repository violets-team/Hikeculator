package com.example.hikeculator.domain.entities

data class DayMeal(
    val products: List<Product>,
) {

    val totalCalories: Long
        get() = products.map { product -> product.nutritionalValue.calories }.sum()

    val totalWeight: Long
        get() = products.map { product -> product.weight }.sum()

    val totalProteins: Long
        get() = products.map { product -> product.nutritionalValue.proteins }.sum()

    val totalFats: Long
        get() = products.map { product -> product.nutritionalValue.fats }.sum()

    val totalCarbs: Long
        get() = products.map { product -> product.nutritionalValue.carbs }.sum()
}