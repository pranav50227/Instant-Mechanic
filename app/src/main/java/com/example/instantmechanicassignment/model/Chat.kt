package com.example.instantmechanicassignment.model

data class Chat(
    val chatId: String,
    val bookingId: String,
    val mechanicName: String,
    val mechanicImage: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int
)
