package com.example.hikeculator.data.common

import com.example.hikeculator.data.entities.FirestoreTrip
import com.example.hikeculator.domain.entities.Trip

fun FirestoreTrip.mapToTrip() = Trip(
    name = name,
    startDate = startDate,
    endDate = endDate,
    userCount = userCount,
    totalCalories = totalCalories,
    type = type,
    difficultyCategory = difficultyCategory,
    season = season
)