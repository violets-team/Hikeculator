package com.example.hikeculator.domain.entities

import com.example.hikeculator.domain.enums.TripType

data class Trip(
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val userCount: Int,
    val totalCalories: Double,
    val type: TripType,
    val difficultyCategory: TripDifficultyCategory,
    val season: String,
)
