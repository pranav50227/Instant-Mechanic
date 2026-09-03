package com.example.instantmechanicassignment.data.model

data class RequestService(
    val mechanicId: String,
    val serviceId: String,
    val customerName: String,
    val customerPhone: String
)
