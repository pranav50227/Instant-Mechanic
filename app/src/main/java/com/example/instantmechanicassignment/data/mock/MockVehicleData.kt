package com.example.instantmechanicassignment.data.mock

import com.example.instantmechanicassignment.model.Vehicle

object MockVehicleData {
    val vehicles = mutableListOf(
        Vehicle(
            vehicleId = "V100",
            registrationNumber = "KA-01-ME-1234",
            company = "Honda",
            model = "Civic",
            manufactureYear = 2020,
            fuelType = "Petrol",
            color = "Black"
        ),
        Vehicle(
            vehicleId = "V101",
            registrationNumber = "KA-05-AB-5678",
            company = "Hyundai",
            model = "i20",
            manufactureYear = 2022,
            fuelType = "Petrol",
            color = "White"
        )
    )
}
