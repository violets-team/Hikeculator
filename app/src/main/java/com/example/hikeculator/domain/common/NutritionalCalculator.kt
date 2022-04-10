package com.example.hikeculator.domain.common

import com.example.hikeculator.domain.enums.MealType
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.Gender
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripSeason
import com.example.hikeculator.domain.enums.TripType
import java.util.concurrent.TimeUnit

object NutritionalCalculator {

    private const val WEIGHT_FACTOR = 10
    private const val HEIGHT_FACTOR = 6.25
    private const val AGE_FACTOR = 5
    private const val MAN_GENDER_FACTOR = -161
    private const val WOMAN_GENDER_FACTOR = 5
    private const val PHYSICAL_LOAD_FACTOR = 1.55
    private const val PROTEINS_NORM_PERCENTAGE = 0.3
    private const val FAT_NORM_PERCENTAGE = 0.3
    private const val CARBS_NORM_PERCENTAGE = 0.4
    const val CALORIES_PER_GRAM_OF_CARBS = 4
    const val CALORIES_PER_GRAM_OF_PROTEINS = 4
    const val CALORIES_PER_GRAM_OF_FAT = 9

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
    ): Double {
        val memberCalorieNormSum = members.sumOf { user -> user.calorieNorm }
        return memberCalorieNormSum *
                difficultyCategory.factor *
                type.factor *
                season.factor *
                dayQuantity
    }

    fun recalculateTripCalorieNorm(trip: Trip, vararg members: User): Double {
        return calculateTripCalorieNorm(
            type = trip.type,
            difficultyCategory = trip.difficultyCategory,
            season = trip.season,
            dayQuantity = getDayCount(startDate = trip.startDate, endDate = trip.endDate),
            members = members
        )
    }

    fun getProteinsNorm(calories: Double): Double {
        return calories * PROTEINS_NORM_PERCENTAGE
    }

    fun getFatNorm(calories: Double): Double {
        return calories * FAT_NORM_PERCENTAGE
    }

    fun getCarbNorm(calories: Double): Double {
        return calories * CARBS_NORM_PERCENTAGE
    }

    fun getMealCaloriesNorm(trip: Trip, mealType: MealType): Double {
        val daysNumber = getDayCount(
            startDate = trip.startDate,
            endDate = trip.endDate
        )

        val caloriesDayNorm = trip.totalCalories / daysNumber

        return caloriesDayNorm * mealType.mealPercentage
    }

    fun getProteinsNormInGrams(calories: Double): Double {
        return getProteinsNorm(calories = calories) / CALORIES_PER_GRAM_OF_PROTEINS
    }

    fun getFatNormInGrams(calories: Double): Double {
        return getFatNorm(calories = calories) / CALORIES_PER_GRAM_OF_FAT
    }

    fun getCarbsNormInGrams(calories: Double): Double {
        return getCarbNorm(calories = calories) / CALORIES_PER_GRAM_OF_CARBS
    }

    private fun getDayCount(startDate: Long, endDate: Long): Int {
        return TimeUnit.MILLISECONDS.toDays(endDate - startDate).inc().toInt()
    }
}