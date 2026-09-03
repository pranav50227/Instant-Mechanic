package com.example.instantmechanicassignment.domain.repository

import com.example.instantmechanicassignment.model.User
import com.example.instantmechanicassignment.model.PaymentMethod

interface ProfileRepository {
    suspend fun getProfile(): Result<User>
    suspend fun updateProfile(user: User): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun updateNotificationSettings(enabled: Boolean): Result<Unit>
    suspend fun updatePrivacySettings(isPublic: Boolean): Result<Unit>
    suspend fun getPaymentMethods(): Result<List<PaymentMethod>>
    suspend fun addPaymentMethod(paymentMethod: PaymentMethod): Result<Unit>
}
