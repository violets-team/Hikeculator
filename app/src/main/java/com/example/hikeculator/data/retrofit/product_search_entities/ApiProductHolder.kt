package com.example.hikeculator.data.retrofit.product_search_entities

import com.google.gson.annotations.SerializedName


data class ApiProductHolder(
    @SerializedName("food")
    val product: ApiProduct
)