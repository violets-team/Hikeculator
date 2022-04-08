package com.example.hikeculator.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductSearchAutoCompleteService {

    val apiService: ProductSearchAutoCompleteApi by lazy {
        buildRetrofit().create(ProductSearchAutoCompleteApi::class.java)
    }

    private fun buildRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}