package com.example.instantmechanicassignment.model

data class Vehicle(
    val vehicleId: String,
    val registrationNumber: String,
    val company: String,
    val model: String,
    val manufactureYear: Int,
    val fuelType: String,
    val color: String
)
