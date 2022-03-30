package com.example.hikeculator.domain.entities

import com.example.hikeculator.domain.enums.Gender

data class User(
    val uid: String,
    val name: String,
    val email: String,
    val age: Int,
    val weight: Double,
    val height: Int,
    val gender: Gender,
    val calorieNorm: Long,
    val tripIds: Set<String> = emptySet()
)
