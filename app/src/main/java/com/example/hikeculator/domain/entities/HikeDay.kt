package com.example.hikeculator.domain.entities

data class HikeDay(
    val breakfast: DayMeal,
    val lunch: DayMeal,
    val dinner: DayMeal,
    val snack: DayMeal,
)
