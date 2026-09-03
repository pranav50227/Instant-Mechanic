package com.example.instantmechanicassignment.data.repository

import com.example.instantmechanicassignment.data.mock.MockBookingData
import com.example.instantmechanicassignment.data.mock.MockMechanicData
import com.example.instantmechanicassignment.data.mock.MockVehicleData
import com.example.instantmechanicassignment.data.remote.ApiService
import com.example.instantmechanicassignment.domain.repository.BookingRepository
import com.example.instantmechanicassignment.model.Booking
import com.example.instantmechanicassignment.model.Mechanic
import com.example.instantmechanicassignment.model.ServiceRequest
import com.example.instantmechanicassignment.model.Vehicle
import com.example.instantmechanicassignment.model.WorkingHours
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class BookingRepositoryImpl(
    private val apiService: ApiService
) : BookingRepository {

    override suspend fun getBookings(): Result<List<Booking>> = withContext(Dispatchers.IO) {
        try {
            delay(1000)
            Result.success(MockBookingData.bookings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBookingDetails(bookingId: String): Result<Booking> = withContext(Dispatchers.IO) {
        try {
            delay(800)
            val booking = MockBookingData.bookings.find { it.bookingId == bookingId }
            if (booking != null) {
                Result.success(booking)
            } else {
                Result.failure(Exception("Booking not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createBooking(request: ServiceRequest): Result<Booking> = withContext(Dispatchers.IO) {
        try {
            delay(1500)
            val newBooking = Booking(
                bookingId = "BK${System.currentTimeMillis()}",
                requestId = request.requestId,
                mechanic = MockMechanicData.mechanics.find { it.mechanicId == request.mechanicId } ?: MockMechanicData.mechanics[0],
                vehicle = MockVehicleData.vehicles.find { it.vehicleId == request.vehicleId } ?: MockVehicleData.vehicles[0],
                appointmentDate = request.preferredDate,
                appointmentTime = request.preferredTime,
                estimatedCost = 1200.0,
                bookingStatus = "Pending"
            )
            MockBookingData.bookings.add(newBooking)
            Result.success(newBooking)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelBooking(bookingId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            delay(1000)
            MockBookingData.bookings.removeIf { it.bookingId == bookingId }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun rescheduleBooking(bookingId: String, date: String, time: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            delay(1000)
            val index = MockBookingData.bookings.indexOfFirst { it.bookingId == bookingId }
            if (index != -1) {
                val updated = MockBookingData.bookings[index].copy(
                    appointmentDate = date, 
                    appointmentTime = time
                )
                MockBookingData.bookings[index] = updated
                Result.success(Unit)
            } else {
                Result.failure(Exception("Booking not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
