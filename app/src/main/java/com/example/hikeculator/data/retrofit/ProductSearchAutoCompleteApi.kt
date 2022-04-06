package com.example.hikeculator.data.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface ProductSearchAutoCompleteApi {

    @GET("/auto-complete")
    suspend fun fetchHints(
        @Query(value = "q") searchExpression: String,
        @Query(value = "app_id") appId: String = APPLICATION_ID,
        @Query(value = "app_key") appKey: String = APPLICATION_KEY,
        @Query(value = "limit") limit: Int = AUTO_COMPLETE_RESULT_LIMIT
    ): List<String>
}