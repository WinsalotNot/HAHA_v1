package com.example.servigo

import com.example.servigo.Data.ApiResponse
import com.example.servigo.Data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("registerUser") // Replace with the actual path on your server
    suspend fun registerUser(@Body user: User): Response<ApiResponse>
}