package com.example.hikeculator.domain.common

import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.Gender
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripSeason
import com.example.hikeculator.domain.enums.TripType

object NutritionalCalculator {

    private const val WEIGHT_FACTOR = 10
    private const val HEIGHT_FACTOR = 6.25
    private const val AGE_FACTOR = 5
    private const val MAN_GENDER_FACTOR = -161
    private const val WOMAN_GENDER_FACTOR = 5
    private const val PHYSICAL_LOAD_FACTOR = 1.55

    fun calculateCalorieNorm(weight: Double, height: Int, age: Int, gender: Gender): Long {
        val genderFactor = if (gender == Gender.MAN) MAN_GENDER_FACTOR else WOMAN_GENDER_FACTOR

        return (((WEIGHT_FACTOR * weight) +
                (HEIGHT_FACTOR * height) -
                (AGE_FACTOR * age) +
                genderFactor
                ) * PHYSICAL_LOAD_FACTOR
                ).toLong()
    }

    fun calculateTripCalorieNorm(
        type: TripType,
        difficultyCategory: TripDifficultyCategory,
        season: TripSeason,
        dayQuantity: Int,
        vararg members: User,
    ): Long {
        val memberCalorieNormSum = members.sumOf { user -> user.calorieNorm }
        return (memberCalorieNormSum *
                difficultyCategory.factor *
                type.factor *
                season.factor *
                dayQuantity
                ).toLong()
    }
}