package com.example.hikeculator.data.fiebase.entities

data class FirestoreDayMeal(
    val products: List<FirestoreProduct> = emptyList(),
)