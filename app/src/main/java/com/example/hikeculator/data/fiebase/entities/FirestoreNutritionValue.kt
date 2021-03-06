package com.example.hikeculator.data.fiebase.entities

import com.example.hikeculator.data.fiebase.DEFAULT_DOUBLE_VALUE

data class FirestoreNutritionValue(
    val calories: Double = DEFAULT_DOUBLE_VALUE,
    val proteins: Double = DEFAULT_DOUBLE_VALUE,
    val fats: Double = DEFAULT_DOUBLE_VALUE,
    val carbohydrates: Double = DEFAULT_DOUBLE_VALUE,
)