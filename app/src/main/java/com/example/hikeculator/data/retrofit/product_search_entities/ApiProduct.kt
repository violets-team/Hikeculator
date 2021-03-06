package com.example.hikeculator.data.retrofit.product_search_entities

import com.google.gson.annotations.SerializedName

data class ApiProduct(
    @SerializedName("foodId")
    val id: String,
    @SerializedName("label")
    val name: String,
    @SerializedName("nutrients")
    val nutritionalValue: ApiNutritionalValue,
)