package com.example.instantmechanicassignment.data.repository

import com.example.instantmechanicassignment.data.mock.MockVehicleData
import com.example.instantmechanicassignment.data.remote.ApiService
import com.example.instantmechanicassignment.domain.repository.VehicleRepository
import com.example.instantmechanicassignment.model.Vehicle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VehicleRepositoryImpl(
    private val apiService: ApiService
) : VehicleRepository {

    override suspend fun getVehicles(): Result<List<Vehicle>> = withContext(Dispatchers.IO) {
        try {
            Result.success(MockVehicleData.vehicles.toList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addVehicle(vehicle: Vehicle): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            MockVehicleData.vehicles.add(vehicle)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeVehicle(vehicleId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            MockVehicleData.vehicles.removeAll { it.vehicleId == vehicleId }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateVehicle(vehicle: Vehicle): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val index = MockVehicleData.vehicles.indexOfFirst { it.vehicleId == vehicle.vehicleId }
            if (index != -1) {
                MockVehicleData.vehicles[index] = vehicle
                Result.success(Unit)
            } else {
                Result.failure(Exception("Vehicle not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
