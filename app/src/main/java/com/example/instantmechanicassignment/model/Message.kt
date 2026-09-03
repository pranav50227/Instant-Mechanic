package com.example.instantmechanicassignment.model

data class Message(
    val messageId: String,
    val chatId: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val sentTime: String,
    val messageType: String,
    val isRead: Boolean
)
