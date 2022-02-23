package com.example.hikeculator.data.entities

import com.example.hikeculator.domain.entities.TripDifficultyCategory
import com.example.hikeculator.domain.enums.Seasons
import com.example.hikeculator.domain.enums.TripType

data class FirestoreTrip(
    val id: String,
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val userCount: Int,
    val totalCalories: Double,
    val type: TripType,
    val difficultyCategory: TripDifficultyCategory,
    val season: Seasons,
)