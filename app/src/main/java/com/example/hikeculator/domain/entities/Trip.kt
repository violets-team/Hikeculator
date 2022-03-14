package com.example.hikeculator.domain.entities

import com.example.hikeculator.domain.enums.TripSeason
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripType

data class Trip(
    val id: String,
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val memberUids: Set<String>,
    val totalCalories: Long,
    val type: TripType,
    val difficultyCategory: TripDifficultyCategory,
    val season: TripSeason,
)
