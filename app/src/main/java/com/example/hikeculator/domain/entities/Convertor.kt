package com.example.hikeculator.domain.entities

class Convertor {

    fun getTotalColorizes(product: Product): Long = product.nutritionalValue.calories

    fun getTotalColorizes(dayMeal: DayMeal): Long = dayMeal.totalCalories
}