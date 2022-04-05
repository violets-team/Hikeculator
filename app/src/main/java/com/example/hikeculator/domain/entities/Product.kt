package com.example.hikeculator.domain.entities

import java.util.*

data class Product(
    val name: String,
    val weight: Long,
    val nutritionalValue: NutritionalValue,
    val id: String = UUID.randomUUID().toString()
) {

    fun getProductWithCalculatedNutritionalValue(weight: Long) = copy(
        weight = weight,
        nutritionalValue = nutritionalValue.getCalculatedNutritionalValue(weight)
    )
}
