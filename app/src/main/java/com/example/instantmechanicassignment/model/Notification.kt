package com.example.instantmechanicassignment.model

data class Notification(
    val notificationId: String,
    val title: String,
    val description: String,
    val createdTime: String,
    val isRead: Boolean
)
