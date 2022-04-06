package com.example.hikeculator.domain.enums

enum class MealType(val mealPercentage: Double) {
    BREAKFAST(mealPercentage = 0.35),
    LUNCH(mealPercentage = 0.30),
    DINNER(mealPercentage = 0.25),
    SNACK(mealPercentage = 0.10),
}