package com.example.instantmechanicassignment.domain.repository

import com.example.instantmechanicassignment.model.Booking
import com.example.instantmechanicassignment.model.ServiceRequest

interface BookingRepository {
    suspend fun getBookings(): Result<List<Booking>>
    suspend fun getBookingDetails(bookingId: String): Result<Booking>
    suspend fun createBooking(request: ServiceRequest): Result<Booking>
    suspend fun cancelBooking(bookingId: String): Result<Unit>
    suspend fun rescheduleBooking(bookingId: String, date: String, time: String): Result<Unit>
}
