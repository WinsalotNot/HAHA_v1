package com.example.HAHA

import com.example.HAHA.Data.ApiResponse
import com.example.HAHA.Data.AvailabilityResponse
import com.example.HAHA.Data.PaymentRequest
import com.example.HAHA.Data.PostingData
import com.example.HAHA.Data.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @GET("/api/availability/{userId}getAvailability")
    suspend fun getAvailability(@Path("userId") userId: Int): Response<AvailabilityResponse>

    @Multipart
    @POST("/api/posting/create")
    suspend fun createPost(
        @Part file: MultipartBody.Part,
        @Part("name") name: String,
        @Part("title") title: String,
        @Part("description") description: String,
        @Part("rank") rank: String,
        @Part("rating") rating: Float,
        @Part("review") review: Int,
        @Part("addr") addr: String,
        @Part("fee") fee: Float,
        @Part("cat") cat: String,
        @Part("shortDesc") shortDesc: String,
        @Part("creatorid") creatorid: Int) : Response<ApiResponse>

    @GET("/api/posting/getAllPosts")
    suspend fun getAllPosts(): Response<List<PostingData>>
}
