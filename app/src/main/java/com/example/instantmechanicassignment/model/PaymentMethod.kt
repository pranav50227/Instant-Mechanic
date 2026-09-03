package com.example.instantmechanicassignment.model

data class PaymentMethod(
    val paymentId: String,
    val cardHolderName: String,
    val lastFourDigits: String,
    val expiryDate: String,
    val cardType: String
)
