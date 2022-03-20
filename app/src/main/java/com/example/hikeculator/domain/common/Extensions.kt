package com.example.hikeculator.domain.common

import com.example.hikeculator.domain.entities.DayMeal
import com.example.hikeculator.domain.entities.TripDay

private const val ONE_HUNDRED_PERCENT = 100
private const val ZERO = 0

fun <T> MutableList<T>.update(newData: List<T>) {
    clear()
    addAll(newData)
}

fun List<TripDay>.getProvisionCalories(): Long {
    return retrieveMeals()
        .map { dayMeal -> dayMeal.totalCalories }
        .sum()
}

fun List<TripDay>.getProvisionWeight(): Long {
    return retrieveMeals()
        .map { dayMeal -> dayMeal.totalWeight }
        .sum()
}

fun List<TripDay>.getProvisionProteinAmount(): Long {
    return retrieveMeals()
        .map { dayMeal -> dayMeal.totalProteins }
        .sum()
}

fun List<TripDay>.getProvisionFatAmount(): Long {
    return retrieveMeals()
        .map { dayMeal -> dayMeal.totalFats }
        .sum()
}

fun List<TripDay>.getProvisionCarbsAmount(): Long {
    return retrieveMeals()
        .map { dayMeal -> dayMeal.totalCarbs }
        .sum()
}

fun List<TripDay>.retrieveMeals(): List<DayMeal> {
    return this.map { day -> listOf(day.breakfast, day.lunch, day.dinner, day.snack) }
        .flatten()
}

infix fun Long.percentageOf(totalValue: Long): Int {
    if (totalValue == ZERO.toLong()) return ZERO
    return ((this * ONE_HUNDRED_PERCENT) / totalValue).toInt()
}