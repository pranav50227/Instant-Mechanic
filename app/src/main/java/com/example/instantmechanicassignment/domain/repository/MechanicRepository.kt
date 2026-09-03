package com.example.instantmechanicassignment.domain.repository

import com.example.instantmechanicassignment.model.Mechanic
import com.example.instantmechanicassignment.model.Service

interface MechanicRepository {
    suspend fun getMechanics(): Result<List<Mechanic>>
    suspend fun searchMechanics(query: String): Result<List<Mechanic>>
    suspend fun filterMechanics(serviceName: String): Result<List<Mechanic>>
    suspend fun getMechanicDetails(mechanicId: String): Result<Mechanic>
    suspend fun getServices(): Result<List<Service>>
}
