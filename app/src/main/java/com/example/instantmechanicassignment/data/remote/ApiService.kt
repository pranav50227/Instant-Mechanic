package com.example.instantmechanicassignment.data.remote

import com.example.instantmechanicassignment.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): AuthResponse
}

data class LoginRequest(val email: String, val password: String)
data class SignupRequest(val fullName: String, val email: String, val phoneNumber: String, val city: String, val password: String)
data class AuthResponse(val token: String, val user: User)
