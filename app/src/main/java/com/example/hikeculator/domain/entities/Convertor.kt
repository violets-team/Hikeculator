package com.example.hikeculator.domain.entities

class Convertor {

    fun getTotalColorizes(product: Product): Double = product.nutritionalValue.calories

    fun getTotalColorizes(dayMeal: DayMeal): Double = dayMeal.totalCalories
}