package com.example.hikeculator.data.retrofit

import com.example.hikeculator.data.common.APPLICATION_ID
import com.example.hikeculator.data.common.APPLICATION_KEY
import com.example.hikeculator.data.entities.api_edaman.FoodSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface IFoodSearchAPI {

    @GET("api/food-database/v2/parser")
    suspend fun searchFood(
        @Query(value = "app_id") appId: String = APPLICATION_ID,
        @Query(value = "app_key") appKey: String = APPLICATION_KEY,
        @Query(value = "ingr") searchExpression: String
    ): FoodSearchResponse
}