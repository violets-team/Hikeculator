package com.example.hikeculator.domain.entities

data class User(
    val name: String,
    val email: String,
    val password: String,
    val token: String,
    val age: Int,
    val weight: Double,
    val height: Double,
    val gender: String,
    val calorieNorm: Long,
)
