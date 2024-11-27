package com.example.HAHA

import com.example.HAHA.Data.ApiResponse
import com.example.HAHA.Data.PaymentRequest
import com.example.HAHA.Data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // Register user
    @POST("api/registerUser")
    suspend fun registerUser(@Body user: User): Response<ApiResponse>

    // Login user
    @POST("api/login")
    suspend fun loginUser(@Body user: User): Response<ApiResponse>

    @POST("api/payment/createTransaction")
    suspend fun createTransaction(@Body paymentRequest: PaymentRequest): Response<ApiResponse>

    @GET("/api/wallet/{userId}getWalletAmount")
    suspend fun getWalletAmount(@Path("userId") userId: Int): Response<Double>
}
