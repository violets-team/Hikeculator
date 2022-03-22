package com.example.hikeculator.data.retrofit

import android.util.Log
import com.example.hikeculator.data.common.BASE_URL
import com.example.hikeculator.data.common.mapToProductList
import com.example.hikeculator.data.entities.api_edaman.ApiProductHolder
import com.example.hikeculator.data.entities.api_edaman.FoodSearchResponse
import com.example.hikeculator.domain.entities.Product
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FoodSearchAPI {

    suspend fun search(searchExpression: String): List<Product> {
        if (isStringEmpty(searchExpression)) { return listOf() }

        val retrofit = getRetrofit()
        val service = retrofit.create(IFoodSearchAPI::class.java)
        val searchResponse: FoodSearchResponse =
            service.searchFood(searchExpression = searchExpression)

        return searchResponse.mapToProductList()
    }

    private fun getRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun isStringEmpty(text: String): Boolean = text.trim().isEmpty()

}