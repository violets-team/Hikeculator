package com.example.hikeculator.data.retrofit.entities

import com.google.gson.annotations.SerializedName


data class ApiProductHolder(
    @SerializedName("food")
    val product: ApiProduct
)