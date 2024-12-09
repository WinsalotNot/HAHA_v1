package com.example.HAHA

import com.example.HAHA.Data.ApiResponse
import com.example.HAHA.Data.AvailabilityResponse
import com.example.HAHA.Data.ChangePassReq
import com.example.HAHA.Data.PaymentRequest
import com.example.HAHA.Data.PayoutData
import com.example.HAHA.Data.PostingData
import com.example.HAHA.Data.PurchaseData
import com.example.HAHA.Data.RankResponse
import com.example.HAHA.Data.TransferData
import com.example.HAHA.Data.User
import com.example.HAHA.Data.WithdrawResponse
import com.example.HAHA.Data.XpResponse
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

    @GET("/api/withdrawdata/{userId}getWithdrawData")
    suspend fun getWithdrawData(@Path("userId") userId: Int): Response<WithdrawResponse>

    @GET("/api/xp/{userId}getXp")
    suspend fun getXp(@Path("userId") userId: Int): Response<XpResponse>

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

    @GET("/api/posting/getAllAvailablePosts")
    suspend fun getAllAvailablePosts(): Response<List<PostingData>>

    @GET("/api/posting/{userId}getAllBoughtPosts")
    suspend fun getAllBoughtPosts(@Path("userId") userId: Int): Response<List<PostingData>>

    @POST("/api/transfer")
    suspend fun transfer(@Body transferData: TransferData): Response<ApiResponse>

    @POST("/api/payment/payoutnow")
    suspend fun payoutNow(@Body payoutData: PayoutData): Response<ApiResponse>

    @POST("/api/resetPassword")
    suspend fun resetPassword(@Body changepassreq: ChangePassReq): Response<ApiResponse>

    @POST("/api/posting/purchaseService")
    suspend fun purchaseService(@Body purchaseData: PurchaseData): Response<ApiResponse>

    @POST("/api/{userId}transferConfirm")
    suspend fun transferConfirm(@Path("userId") userId: Int): Response<ApiResponse>

    @GET("/api/rank/getAllUserByRank")
    suspend fun getAllUserByRank(): Response<List<RankResponse>>
}
