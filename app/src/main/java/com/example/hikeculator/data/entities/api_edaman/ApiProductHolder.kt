package com.example.hikeculator.data.entities.api_edaman

import com.google.gson.annotations.SerializedName


data class ApiProductHolder(
    @SerializedName("food")
    val product: ApiProduct
)