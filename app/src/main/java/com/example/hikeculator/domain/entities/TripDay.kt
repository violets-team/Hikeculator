package com.example.hikeculator.domain.entities

data class TripDay(
    val id: String,
    val date: Long,
    val breakfast: DayMeal,
    val lunch: DayMeal,
    val dinner: DayMeal,
    val snack: DayMeal,
) {

    fun getTotalCalories(): Double {
        return breakfast.totalCalories +
                lunch.totalCalories +
                dinner.totalCalories +
                snack.totalCalories
    }

    fun getProvisionProteinAmountPerGram(): Double {
        return breakfast.totalProteins +
                lunch.totalProteins +
                dinner.totalProteins +
                snack.totalProteins
    }

    fun getProvisionFatAmountPerGram(): Double {
        return breakfast.totalFats +
                lunch.totalFats +
                dinner.totalFats +
                snack.totalFats
    }

    fun getProvisionCarbsAmountPerGram(): Double {
        return breakfast.totalCarbs +
                lunch.totalCarbs +
                dinner.totalCarbs +
                snack.totalCarbs
    }
}
