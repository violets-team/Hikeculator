package com.example.hikeculator.domain.entities

data class TripDay(
    val id: String,
    val breakfast: DayMeal,
    val lunch: DayMeal,
    val dinner: DayMeal,
    val snack: DayMeal,
)
