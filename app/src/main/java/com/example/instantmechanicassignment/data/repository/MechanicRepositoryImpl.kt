package com.example.instantmechanicassignment.data.repository

import com.example.instantmechanicassignment.data.mock.MockMechanicData
import com.example.instantmechanicassignment.data.remote.ApiService
import com.example.instantmechanicassignment.domain.repository.MechanicRepository
import com.example.instantmechanicassignment.model.Mechanic
import com.example.instantmechanicassignment.model.Service
import com.example.instantmechanicassignment.model.WorkingHours
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MechanicRepositoryImpl(
    private val apiService: ApiService
) : MechanicRepository {

    override suspend fun getMechanics(): Result<List<Mechanic>> = withContext(Dispatchers.IO) {
        try {
            delay(1000)
            Result.success(MockMechanicData.mechanics)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchMechanics(query: String): Result<List<Mechanic>> = withContext(Dispatchers.IO) {
        try {
            delay(500)
            val filtered = MockMechanicData.mechanics.filter { 
                it.garageName.contains(query, ignoreCase = true) || 
                it.address.contains(query, ignoreCase = true) ||
                it.services.any { service -> service.serviceName.contains(query, ignoreCase = true) }
            }
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun filterMechanics(serviceName: String): Result<List<Mechanic>> = withContext(Dispatchers.IO) {
        try {
            delay(500)
            val filtered = MockMechanicData.mechanics.filter { 
                it.services.any { service -> service.serviceName.contains(serviceName, ignoreCase = true) }
            }
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMechanicDetails(mechanicId: String): Result<Mechanic> = withContext(Dispatchers.IO) {
        try {
            delay(800)
            val mechanic = MockMechanicData.mechanics.find { it.mechanicId == mechanicId }
            if (mechanic != null) {
                Result.success(mechanic)
            } else {
                Result.failure(Exception("Mechanic not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getServices(): Result<List<Service>> = withContext(Dispatchers.IO) {
        try {
            delay(500)
            Result.success(MockMechanicData.services)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
