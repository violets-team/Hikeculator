package com.example.hikeculator.domain.enums

import com.example.hikeculator.domain.entities.TripDifficultyCategory.*


enum class TripType(
    val difficultyCategory1: Category1,
    val difficultyCategory2: Category2,
    val difficultyCategory3: Category3,
    val difficultyCategory4: Category4,
    val difficultyCategory5: Category5,
    val difficultyCategory6: Category6,
) {

    HIKE(
        difficultyCategory1 = Category1(factor = 1.0),
        difficultyCategory2 = Category2(factor = 1.097),
        difficultyCategory3 = Category3(factor = 1.193),
        difficultyCategory4 = Category4(factor = 1.322),
        difficultyCategory5 = Category5(factor = 1.45),
        difficultyCategory6 = Category6(factor = 1.612)
    ),

    WATER(
        difficultyCategory1 =  Category1(factor = 0.8),
        difficultyCategory2 =  Category2(factor = 0.877),
        difficultyCategory3 =  Category3(factor = 0.954),
        difficultyCategory4 =  Category4(factor = 1.19),
        difficultyCategory5 =  Category5(factor = 1.3),
        difficultyCategory6 =  Category6(factor = 1.45)
    ),

    MOUNTAIN(
        difficultyCategory1 =  Category1(factor = 1.3),
        difficultyCategory2 =  Category2(factor = 1.425),
        difficultyCategory3 =  Category3(factor = 1.55),
        difficultyCategory4 =  Category4(factor = 1.719),
        difficultyCategory5 =  Category5(factor = 1.887),
        difficultyCategory6 =  Category6(factor = 2.096)
    ),

    SKI(
        difficultyCategory1 =  Category1(factor = 1.2),
        difficultyCategory2 =  Category2(factor = 1.316),
        difficultyCategory3 =  Category3(factor = 1.432),
        difficultyCategory4 =  Category4(factor = 1.587),
        difficultyCategory5 =  Category5(factor = 1.741),
        difficultyCategory6 =  Category6(factor = 1.935)
    )
}