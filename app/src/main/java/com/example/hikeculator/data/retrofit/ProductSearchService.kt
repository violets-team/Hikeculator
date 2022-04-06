package com.example.hikeculator.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductSearchService {

    val apiService: ProductSearchApi by lazy { buildRetrofit().create(ProductSearchApi::class.java) }

    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}