package com.example.instantmechanicassignment.model

data class Service(
    val serviceId: String,
    val serviceName: String,
    val icon: String,
    val estimatedPrice: Double,
    val estimatedDuration: String
)
