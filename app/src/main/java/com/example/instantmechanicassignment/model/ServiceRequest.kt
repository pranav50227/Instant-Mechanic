package com.example.instantmechanicassignment.model

data class ServiceRequest(
    val requestId: String,
    val userId: String,
    val mechanicId: String,
    val vehicleId: String,
    val serviceId: String,
    val preferredDate: String,
    val preferredTime: String,
    val problemDescription: String,
    val status: String
)
