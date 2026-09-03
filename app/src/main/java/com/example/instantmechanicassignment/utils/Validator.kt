package com.example.instantmechanicassignment.utils

object Validator {
    fun isValidPhone(phone: String): Boolean {
        return phone.length >= 10
    }
}
