package com.example.HAHA.Data

data class ApiResponse(
    val success : Boolean,
    val message: String,
    val userId : Int,
    val token : String,
    val redirectUrl : String,
    val name: String,
    val address: String
)

data class AvailabilityResponse(
    val availability: String,
    val hasBenefAcc: Boolean
)

data class RankResponse(
    val rank: String,
    val name: String
)

data class WithdrawResponse(
    val withdrawQuota: Double,
    val withdrawTries: Int
)

data class XpResponse(
    val rank: String,
    val xp: Int
)


