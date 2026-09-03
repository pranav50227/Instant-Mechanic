package com.example.instantmechanicassignment.model

data class Mechanic(
    val mechanicId: String,
    val garageName: String,
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int,
    val distance: Double,
    val location: String,
    val address: String,
    val phoneNumber: String,
    val isOpen: Boolean,
    val openingHours: WorkingHours,
    val services: List<Service>,
    val latitude: Double,
    val longitude: Double
)
