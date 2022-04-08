package com.example.hikeculator.data.retrofit

import com.example.hikeculator.data.retrofit.product_search_entities.ProductSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductSearchApi {

    @GET("api/food-database/v2/parser")
    suspend fun searchFood(
        @Query(value = "ingr") searchExpression: String,
        @Query(value = "app_id") appId: String = APPLICATION_ID,
        @Query(value = "app_key") appKey: String = APPLICATION_KEY
    ): ProductSearchResponse
}