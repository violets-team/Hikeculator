package com.example.hikeculator.data.entities

import com.example.hikeculator.data.common.DEFAULT_DOUBLE_VALUE
import com.example.hikeculator.data.common.DEFAULT_STRING_VALUE

class FirestoreProduct(
    val name: String = DEFAULT_STRING_VALUE,
    val weight: Double = DEFAULT_DOUBLE_VALUE,
    val nutritionalValue: FirestoreNutritionValue = FirestoreNutritionValue(),
)