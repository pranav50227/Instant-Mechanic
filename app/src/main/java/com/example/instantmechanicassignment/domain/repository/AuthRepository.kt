package com.example.instantmechanicassignment.domain.repository

import com.example.instantmechanicassignment.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun signup(fullName: String, email: String, phoneNumber: String, city: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
    
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun deleteToken()
    
    fun isUserLoggedIn(): Flow<Boolean>
    suspend fun validateSession(): Result<Boolean>
    suspend fun getCurrentUser(): Result<User>
}
