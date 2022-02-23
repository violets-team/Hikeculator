package com.example.hikeculator.domain.entities

import com.example.hikeculator.domain.enums.Gender

data class User(
    val name: String,
    val email: String,
    val password: String,
    val age: Int,
    val weight: Double,
    val height: Double,
    val gender: Gender,
    val calorieNorm: Long,
)
