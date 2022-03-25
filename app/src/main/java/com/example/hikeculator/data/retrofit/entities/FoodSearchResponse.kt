package com.example.hikeculator.data.retrofit.entities

import com.google.gson.annotations.SerializedName

data class FoodSearchResponse(
    @SerializedName("hints")
    val productHolders: List<ApiProductHolder>
)