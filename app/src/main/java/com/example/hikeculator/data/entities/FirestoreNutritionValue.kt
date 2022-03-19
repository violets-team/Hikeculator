package com.example.hikeculator.data.entities

import com.example.hikeculator.data.common.DEFAULT_DOUBLE_VALUE

class FirestoreNutritionValue(
    val calories: Double = DEFAULT_DOUBLE_VALUE,
    val proteins: Double = DEFAULT_DOUBLE_VALUE,
    val fats: Double = DEFAULT_DOUBLE_VALUE,
    val carbohydrates: Double = DEFAULT_DOUBLE_VALUE,
)