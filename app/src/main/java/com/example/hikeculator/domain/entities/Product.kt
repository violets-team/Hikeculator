package com.example.hikeculator.domain.entities

import java.util.*

data class Product(
    val name: String,
    val weight: Long,
    val nutritionalValue: NutritionalValue,
    val id: String = UUID.randomUUID().toString()
) {

    fun getCalorieAmount(): Double = nutritionalValue.calories * weight

    fun getProteinAmount(): Double = nutritionalValue.proteins * weight

    fun getFatAmount(): Double = nutritionalValue.fats * weight

    fun getCarbsAmount(): Double = nutritionalValue.carbs * weight
}
