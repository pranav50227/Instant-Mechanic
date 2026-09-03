package com.example.instantmechanicassignment.presentation.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instantmechanicassignment.model.Booking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BookingDetailsUiState {
    object Loading : BookingDetailsUiState()
    data class Success(val booking: Booking) : BookingDetailsUiState()
    data class Error(val message: String) : BookingDetailsUiState()
}

class BookingDetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<BookingDetailsUiState>(BookingDetailsUiState.Loading)
    val uiState: StateFlow<BookingDetailsUiState> = _uiState.asStateFlow()

    fun loadBookingDetails(bookingId: String) {
        viewModelScope.launch {
            _uiState.value = BookingDetailsUiState.Loading
            
            // Simulate repository.getBookingDetails(bookingId)
            delay(1000)
            
            // Mock success logic - normally we'd fetch this from a repository
            // For now, if we don't have the data, we might show an error or a mock
            // Assuming we have a way to get the booking, e.g. from a shared state or API
            _uiState.value = BookingDetailsUiState.Error("Booking not found")
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            _uiState.value = BookingDetailsUiState.Loading
            // Simulate repository.cancelBooking(bookingId)
            delay(1500)
            
            // After cancellation, we might want to update the UI state
            // or navigate back. For now, just reload or show error.
            loadBookingDetails(bookingId)
        }
    }

    fun messageMechanic(mechanicId: String) {
        // Implementation for navigating to Chat screen with this mechanic
    }
}
