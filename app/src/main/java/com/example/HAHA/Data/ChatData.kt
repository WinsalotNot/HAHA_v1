package com.example.HAHA.Data

data class ChatData(
    val senderId: String = "",
    val receiverId: String = "",
    val senderName: String = "",
    val receiverName: String = "",
    val message: String = "",
    val timestamp: Long = 0L
)
