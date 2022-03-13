package com.example.hikeculator.data.entities

import com.example.hikeculator.data.common.DEFAULT_DOUBLE_VALUE
import com.example.hikeculator.data.common.DEFAULT_INT_VALUE
import com.example.hikeculator.data.common.DEFAULT_LONG_VALUE
import com.example.hikeculator.data.common.DEFAULT_STRING_VALUE
import com.example.hikeculator.domain.enums.TripSeason
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripType

data class FirestoreTrip(
    val id: String = DEFAULT_STRING_VALUE,
    val name: String = DEFAULT_STRING_VALUE,
    val startDate: Long = DEFAULT_LONG_VALUE,
    val endDate: Long = DEFAULT_LONG_VALUE,
    val memberUids: List<String> = emptyList(),
    val totalCalories: Double = DEFAULT_DOUBLE_VALUE,
    val type: TripType = TripType.HIKE,
    val difficultyCategory: TripDifficultyCategory = TripDifficultyCategory.CATEGORY_1,
    val season: TripSeason = TripSeason.FALL,
)