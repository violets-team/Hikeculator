package com.example.hikeculator.domain.entities

import java.util.*

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val weight: Long,
    val nutritionalValue: NutritionalValue,
)
