package com.example.hikeculator.domain.entities

class DayMeal(private val products: Set<Product>) {

    val totalCalories: Long
    get() = products.map { it.nutritionalValue.calories }.sum()

    val totalWeight: Long
    get() = products.map { it.weight }.sum()
}