package com.example.hikeculator.domain.entities

sealed class TripDifficultyCategory(val factor: Double) {

    class Category1(factor: Double) : TripDifficultyCategory(factor)
    class Category2(factor: Double) : TripDifficultyCategory(factor)
    class Category3(factor: Double) : TripDifficultyCategory(factor)
    class Category4(factor: Double) : TripDifficultyCategory(factor)
    class Category5(factor: Double) : TripDifficultyCategory(factor)
    class Category6(factor: Double) : TripDifficultyCategory(factor)
}

