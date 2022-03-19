package com.example.hikeculator.data.entities.api_edaman

import com.google.gson.annotations.SerializedName

data class FoodSearchResponse(
    @SerializedName("hints")
    val productHolders: List<ApiProductHolder>
)