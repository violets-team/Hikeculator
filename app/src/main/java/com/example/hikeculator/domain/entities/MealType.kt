package com.example.hikeculator.domain.entities

enum class MealType(val mealPercentage: Double) {
    BREAKFAST(mealPercentage = 0.35),
    LUNCH(mealPercentage = 0.30),
    DINNER(mealPercentage = 0.25),
    SNACK(0.10),
}