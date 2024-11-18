package com.example.servigo

import com.example.servigo.Data.ApiResponse
import com.example.servigo.Data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    // Register user
    @POST("api/registerUser")
    suspend fun registerUser(@Body user: User): Response<ApiResponse>

    // Login user
    @POST("api/login")
    suspend fun loginUser(@Body user: User): Response<ApiResponse>
}
