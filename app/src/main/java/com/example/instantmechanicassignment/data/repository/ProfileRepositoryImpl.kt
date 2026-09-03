package com.example.instantmechanicassignment.data.repository

import com.example.instantmechanicassignment.data.mock.MockProfileData
import com.example.instantmechanicassignment.data.remote.ApiService
import com.example.instantmechanicassignment.domain.repository.ProfileRepository
import com.example.instantmechanicassignment.model.PaymentMethod
import com.example.instantmechanicassignment.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
    private val apiService: ApiService
) : ProfileRepository {

    override suspend fun getProfile(): Result<User> = withContext(Dispatchers.IO) {
        try {
            Result.success(MockProfileData.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(user: User): Result<User> = withContext(Dispatchers.IO) {
        try {
            MockProfileData.user = user
            Result.success(MockProfileData.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateNotificationSettings(enabled: Boolean): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePrivacySettings(isPublic: Boolean): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPaymentMethods(): Result<List<PaymentMethod>> = withContext(Dispatchers.IO) {
        try {
            Result.success(MockProfileData.paymentMethods.toList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addPaymentMethod(paymentMethod: PaymentMethod): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            MockProfileData.paymentMethods.add(paymentMethod)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
