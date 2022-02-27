package com.example.hikeculator.data.entities

import com.example.hikeculator.data.common.DEFAULT_STRING_VALUE

class FirestoreTripDay(
    val id: String = DEFAULT_STRING_VALUE,
    val breakfast: FirestoreDayMeal = FirestoreDayMeal(),
    val lunch: FirestoreDayMeal = FirestoreDayMeal(),
    val dinner: FirestoreDayMeal = FirestoreDayMeal(),
    val snack: FirestoreDayMeal = FirestoreDayMeal(),
)