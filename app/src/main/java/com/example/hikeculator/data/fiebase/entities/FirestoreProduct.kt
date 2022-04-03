package com.example.hikeculator.data.fiebase.entities

import com.example.hikeculator.data.fiebase.DEFAULT_LONG_VALUE
import com.example.hikeculator.data.fiebase.DEFAULT_STRING_VALUE

data class FirestoreProduct(
    val name: String = DEFAULT_STRING_VALUE,
    val weight: Long = DEFAULT_LONG_VALUE,
    val nutritionalValue: FirestoreNutritionValue = FirestoreNutritionValue(),
)