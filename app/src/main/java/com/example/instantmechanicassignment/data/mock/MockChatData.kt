package com.example.instantmechanicassignment.data.mock

import com.example.instantmechanicassignment.model.Chat
import com.example.instantmechanicassignment.model.Message

object MockChatData {
    val chats = listOf(
        Chat(
            chatId = "C101",
            bookingId = "BK100",
            mechanicName = "Apex Auto Care",
            mechanicImage = "",
            lastMessage = "We have received your booking.",
            lastMessageTime = "10:30 AM",
            unreadCount = 2
        ),
        Chat(
            chatId = "C102",
            bookingId = "BK101",
            mechanicName = "Elite Garage",
            mechanicImage = "",
            lastMessage = "Your vehicle is ready.",
            lastMessageTime = "Yesterday",
            unreadCount = 0
        )
    )

    val messages = mapOf(
        "C101" to listOf(
            Message(
                messageId = "MSG1",
                chatId = "C101",
                senderId = "M101",
                receiverId = "U101",
                message = "Good Morning.",
                sentTime = "10:00 AM",
                messageType = "text",
                isRead = true
            ),
            Message(
                messageId = "MSG2",
                chatId = "C101",
                senderId = "U101",
                receiverId = "M101",
                message = "Brake issue.",
                sentTime = "10:05 AM",
                messageType = "text",
                isRead = true
            ),
            Message(
                messageId = "MSG3",
                chatId = "C101",
                senderId = "M101",
                receiverId = "U101",
                message = "Please arrive at 10 AM.",
                sentTime = "10:10 AM",
                messageType = "text",
                isRead = false
            )
        )
    )
}
