package com.example.instantmechanicassignment.model

data class Booking(
    val bookingId: String,
    val requestId: String,
    val mechanic: Mechanic,
    val vehicle: Vehicle,
    val appointmentDate: String,
    val appointmentTime: String,
    val estimatedCost: Double,
    val bookingStatus: String
)
