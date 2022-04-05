package com.example.hikeculator.domain.entities

import java.util.*

data class ProvisionBagProduct(
    val name: String,
    val weight: Long,
    val nutritionalValue: NutritionalValue,
    val isBought: Boolean,
    val id: String = UUID.randomUUID().toString()
)