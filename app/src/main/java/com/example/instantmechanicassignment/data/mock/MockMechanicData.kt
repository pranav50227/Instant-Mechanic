package com.example.instantmechanicassignment.data.mock

import com.example.instantmechanicassignment.model.Mechanic
import com.example.instantmechanicassignment.model.Service
import com.example.instantmechanicassignment.model.WorkingHours

object MockMechanicData {
    val workingHours = WorkingHours(
        monday = "09:00 AM - 08:00 PM",
        tuesday = "09:00 AM - 08:00 PM",
        wednesday = "09:00 AM - 08:00 PM",
        thursday = "09:00 AM - 08:00 PM",
        friday = "09:00 AM - 08:00 PM",
        saturday = "09:00 AM - 08:00 PM",
        sunday = "Closed"
    )

    val services = listOf(
        Service("S1", "Oil Change", "ic_oil", 800.0, "30-45 mins"),
        Service("S2", "Brake Repair", "ic_brake", 1200.0, "1 hour"),
        Service("S3", "Battery Replacement", "ic_battery", 1500.0, "15 mins"),
        Service("S4", "Diagnostics", "ic_diagnostics", 500.0, "20 mins")
    )

    val mechanics = listOf(
        Mechanic(
            mechanicId = "M101",
            garageName = "Apex Auto Care",
            imageUrl = "https://example.com/garage1.jpg",
            rating = 4.8,
            reviewCount = 124,
            distance = 1.2,
            location = "Indiranagar",
            address = "123 Mechanics Street, Indiranagar, Bengaluru",
            phoneNumber = "9876543210",
            isOpen = true,
            openingHours = workingHours,
            services = services,
            latitude = 12.9716,
            longitude = 77.5946
        ),
        Mechanic(
            mechanicId = "M102",
            garageName = "Elite Garage",
            imageUrl = "https://example.com/garage2.jpg",
            rating = 4.6,
            reviewCount = 92,
            distance = 3.1,
            location = "Residency Road",
            address = "456, Residency Road, Bengaluru",
            phoneNumber = "9876543211",
            isOpen = false,
            openingHours = workingHours,
            services = listOf(services[0], services[1]),
            latitude = 12.9650,
            longitude = 77.6000
        ),
        Mechanic(
            mechanicId = "M103",
            garageName = "Precision Motors",
            imageUrl = "https://example.com/garage3.jpg",
            rating = 4.9,
            reviewCount = 210,
            distance = 0.8,
            location = "Koramangala",
            address = "789, Koramangala, Bengaluru",
            phoneNumber = "9876543212",
            isOpen = true,
            openingHours = workingHours,
            services = services,
            latitude = 12.9352,
            longitude = 77.6245
        )
    )
}
