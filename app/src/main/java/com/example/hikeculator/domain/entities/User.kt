package com.example.hikeculator.domain.entities

data class User(
    val name: String,
    val age: Int,
    val weight: Double,
    val height: Double,
    val gender: String,
    val totalCalories: Long,
    val email: String,
    val password: String,
    val token: String
)
