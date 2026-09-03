package com.example.instantmechanicassignment.domain.repository

import com.example.instantmechanicassignment.model.Vehicle

interface VehicleRepository {
    suspend fun getVehicles(): Result<List<Vehicle>>
    suspend fun addVehicle(vehicle: Vehicle): Result<Unit>
    suspend fun removeVehicle(vehicleId: String): Result<Unit>
    suspend fun updateVehicle(vehicle: Vehicle): Result<Unit>
}
