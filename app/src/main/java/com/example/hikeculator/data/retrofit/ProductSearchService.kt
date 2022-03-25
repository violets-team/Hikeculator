package com.example.hikeculator.data.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductSearchService {

    val apiService: ProductSearchApi by lazy { buildRetrofit().create(ProductSearchApi::class.java) }

    private fun buildRetrofit(): Retrofit {
        val loggingInterceptor = getLoggingInterceptor()
        val client = getOkHttpClient(loggingInterceptor)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    private fun getOkHttpClient(interceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder().addInterceptor(interceptor).build()
}