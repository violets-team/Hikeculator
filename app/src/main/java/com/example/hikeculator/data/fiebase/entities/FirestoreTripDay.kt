package com.example.hikeculator.data.fiebase.entities

import com.example.hikeculator.data.fiebase.DEFAULT_LONG_VALUE
import com.example.hikeculator.data.fiebase.DEFAULT_STRING_VALUE

data class FirestoreTripDay(
    val id: String = DEFAULT_STRING_VALUE,
    val date: Long = DEFAULT_LONG_VALUE,
    val breakfast: FirestoreDayMeal = FirestoreDayMeal(),
    val lunch: FirestoreDayMeal = FirestoreDayMeal(),
    val dinner: FirestoreDayMeal = FirestoreDayMeal(),
    val snack: FirestoreDayMeal = FirestoreDayMeal(),
)