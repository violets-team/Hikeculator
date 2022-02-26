package com.example.hikeculator.domain.entities

import com.example.hikeculator.domain.enums.Seasons
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripType

data class Trip(
    val id: String,
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val memberCount: Int,
    val totalCalories: Double,
    val type: TripType,
    val difficultyCategory: TripDifficultyCategory,
    val season: Seasons,
)
