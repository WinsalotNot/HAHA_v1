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


