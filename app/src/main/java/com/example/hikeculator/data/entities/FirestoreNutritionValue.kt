package com.example.hikeculator.data.entities

import com.example.hikeculator.data.common.DEFAULT_LONG_VALUE

class FirestoreNutritionValue(
    val calories: Long = DEFAULT_LONG_VALUE,
    val proteins: Long = DEFAULT_LONG_VALUE,
    val fats: Long = DEFAULT_LONG_VALUE,
    val carbohydrates: Long = DEFAULT_LONG_VALUE,
)