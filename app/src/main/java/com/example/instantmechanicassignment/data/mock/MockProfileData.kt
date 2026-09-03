package com.example.instantmechanicassignment.data.mock

import com.example.instantmechanicassignment.model.PaymentMethod
import com.example.instantmechanicassignment.model.User

object MockProfileData {
    var user = MockAuthData.mockUser

    val paymentMethods = mutableListOf(
        PaymentMethod("P1", "John Doe", "1234", "12/28", "Visa"),
        PaymentMethod("P2", "John Doe", "5678", "05/30", "MasterCard")
    )
}
