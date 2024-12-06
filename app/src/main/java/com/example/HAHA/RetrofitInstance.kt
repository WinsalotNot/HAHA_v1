package com.example.HAHA

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://quetzal-deciding-generally.ngrok-free.app/"

    val apiService: ApiService by lazy {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging) // Attach the logging interceptor
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Pass the OkHttpClient with the interceptor here
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
