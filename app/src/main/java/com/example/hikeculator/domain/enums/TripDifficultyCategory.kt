package com.example.hikeculator.domain.enums

enum class TripDifficultyCategory(val factor: Double) {

    CATEGORY_1(factor = 1.0),
    CATEGORY_2(factor = 1.097),
    CATEGORY_3(factor = 1.193),
    CATEGORY_4(factor = 1.322),
    CATEGORY_5(factor = 1.45),
    CATEGORY_6(factor = 1.612),
}