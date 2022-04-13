package com.example.hikeculator.domain.common

import com.example.hikeculator.domain.common.NutritionalCalculator.CALORIES_PER_GRAM_OF_CARBS
import com.example.hikeculator.domain.common.NutritionalCalculator.CALORIES_PER_GRAM_OF_FAT
import com.example.hikeculator.domain.common.NutritionalCalculator.CALORIES_PER_GRAM_OF_PROTEINS
import com.example.hikeculator.domain.entities.DayMeal
import com.example.hikeculator.domain.entities.TripDay
import kotlin.math.roundToInt

private const val ONE_HUNDRED_PERCENT = 100
private const val ZERO = 0

fun <T> MutableList<T>.update(newData: List<T>) {
    clear()
    addAll(newData)
}

fun List<TripDay>.getProvisionCalories(): Double {
    return retrieveMeals().sumOf { dayMeal -> dayMeal.totalCalories }
}

fun List<TripDay>.getProvisionWeight(): Long {
    return retrieveMeals().sumOf { dayMeal -> dayMeal.totalWeight }
}

fun List<TripDay>.getProvisionProteinAmountPerCalories(): Double {
    return retrieveMeals().sumOf { dayMeal -> dayMeal.totalProteins } * CALORIES_PER_GRAM_OF_PROTEINS
}

fun List<TripDay>.getProvisionFatAmountPerCalories(): Double {
    return retrieveMeals().sumOf { dayMeal -> dayMeal.totalFats } * CALORIES_PER_GRAM_OF_FAT
}

fun List<TripDay>.getProvisionCarbsAmountPerCalories(): Double {
    return retrieveMeals().sumOf { dayMeal -> dayMeal.totalCarbs } * CALORIES_PER_GRAM_OF_CARBS
}

fun List<TripDay>.retrieveMeals(): List<DayMeal> {
    return this.map { day -> listOf(day.breakfast, day.lunch, day.dinner, day.snack) }
        .flatten()
}

infix fun Double.percentageOf(totalValue: Double): Int {
    if (totalValue == ZERO.toDouble()) return ZERO
    return ((this * ONE_HUNDRED_PERCENT) / totalValue).toInt()
}

fun Double.roundToTwoDecimalPlaces(): Double {
    return (this * VALUE_ONE_HUNDRED).roundToInt().toDouble() / VALUE_ONE_HUNDRED
}

fun Double.divideByOneHundred(): Double = div(VALUE_ONE_HUNDRED)

fun String.isEmptyWithoutSpaces(): Boolean = this.trim().isEmpty()