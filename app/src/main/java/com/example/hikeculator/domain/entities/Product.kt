package com.example.hikeculator.domain.entities

data class Product(
    val name: String,
    val weight: Long,
    val nutritionalValue: NutritionalValue,
)
