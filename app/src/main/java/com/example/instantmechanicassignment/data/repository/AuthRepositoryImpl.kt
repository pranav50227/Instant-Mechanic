package com.example.instantmechanicassignment.data.repository

import com.example.instantmechanicassignment.data.local.PreferenceManager
import com.example.instantmechanicassignment.data.mock.MockAuthData
import com.example.instantmechanicassignment.data.remote.ApiService
import com.example.instantmechanicassignment.data.remote.LoginRequest
import com.example.instantmechanicassignment.data.remote.SignupRequest
import com.example.instantmechanicassignment.domain.repository.AuthRepository
import com.example.instantmechanicassignment.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            // Simulated login logic using MockAuthData
            if (email == MockAuthData.mockUser.email && password == "12345678") {
                preferenceManager.saveToken("JWT_123456")
                Result.success(MockAuthData.mockUser)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signup(fullName: String, email: String, phoneNumber: String, city: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            // Simulated signup returning success
            val newUser = User(
                userId = "U${System.currentTimeMillis()}",
                fullName = fullName,
                email = email,
                phoneNumber = phoneNumber,
                city = city,
                profileImage = "",
                createdAt = "2026-02-17"
            )
            preferenceManager.saveToken("JWT_NEW_USER")
            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            preferenceManager.clearToken()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveToken(token: String) {
        preferenceManager.saveToken(token)
    }

    override suspend fun getToken(): String? {
        return preferenceManager.getToken().first()
    }

    override suspend fun deleteToken() {
        preferenceManager.clearToken()
    }

    override fun isUserLoggedIn(): Flow<Boolean> {
        return preferenceManager.getToken().map { it != null }
    }

    override suspend fun validateSession(): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val token = getToken()
            Result.success(token != null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<User> = withContext(Dispatchers.IO) {
        try {
            val token = getToken()
            if (token != null) {
                // In a real app, we would fetch the user associated with this token
                // For mock, we return the mockUser
                Result.success(MockAuthData.mockUser)
            } else {
                Result.failure(Exception("Not logged in"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
