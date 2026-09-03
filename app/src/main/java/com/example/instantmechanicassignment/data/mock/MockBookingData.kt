package com.example.instantmechanicassignment.data.mock

import com.example.instantmechanicassignment.model.Booking

object MockBookingData {
    val bookings = mutableListOf(
        Booking(
            bookingId = "BK100",
            requestId = "R100",
            mechanic = MockMechanicData.mechanics[0],
            vehicle = MockVehicleData.vehicles[0],
            appointmentDate = "24 Sept",
            appointmentTime = "10:30 AM",
            estimatedCost = 800.0,
            bookingStatus = "Pending"
        ),
        Booking(
            bookingId = "BK101",
            requestId = "R101",
            mechanic = MockMechanicData.mechanics[1],
            vehicle = MockVehicleData.vehicles[0],
            appointmentDate = "26 Sept",
            appointmentTime = "10:30 AM",
            estimatedCost = 1200.0,
            bookingStatus = "Confirmed"
        )
    )
}
