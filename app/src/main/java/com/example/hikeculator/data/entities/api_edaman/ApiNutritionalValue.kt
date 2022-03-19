package com.example.hikeculator.data.entities.api_edaman

import com.google.gson.annotations.SerializedName

data class ApiNutritionalValue(
    @SerializedName("ENERC_KCAL")
    val calories: Double,
    @SerializedName("PROCNT")
    val proteins: Double,
    @SerializedName("FAT")
    val fats: Double,
    @SerializedName("CHOCDF")
    val carbohydrates: Double,
)
