package com.example.hikeculator.domain.common

import com.example.hikeculator.domain.entities.DayMeal
import com.example.hikeculator.domain.entities.TripDay
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

private const val ONE_HUNDRED_PERCENT = 100
private const val ZERO = 0

fun <T> MutableList<T>.update(newData: List<T>) {
    clear()
    addAll(newData)
}

fun List<TripDay>.getProvisionCalories(): Double {
    return retrieveMeals()
        .map { dayMeal -> dayMeal.totalCalories }
        .sum()
}

fun List<TripDay>.getProvisionWeight(): Long {
    return retrieveMeals()
        .map { dayMeal -> dayMeal.totalWeight }
        .sum()
}

fun List<TripDay>.getProvisionProteinAmount(): Double {
    return retrieveMeals()
        .map { dayMeal -> dayMeal.totalProteins }
        .sum()
}

fun List<TripDay>.getProvisionFatAmount(): Double {
    return retrieveMeals()
        .map { dayMeal -> dayMeal.totalFats }
        .sum()
}

fun List<TripDay>.getProvisionCarbsAmount(): Double {
    return retrieveMeals()
        .map { dayMeal -> dayMeal.totalCarbs }
        .sum()
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