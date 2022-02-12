package com.example.hikeculator.domain.entities

import java.util.*

data class Hike(
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val userCount: Int,
    val totalCalories: Double,
    val level: String,
    val season: String
)
