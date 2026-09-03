package com.example.instantmechanicassignment.presentation.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instantmechanicassignment.model.Booking
import androidx.lifecycle.ViewModelProvider
import com.example.instantmechanicassignment.domain.repository.BookingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BookingUiState {
    object Loading : BookingUiState()
    data class Success(val bookings: List<Booking>) : BookingUiState()
    object Empty : BookingUiState()
    data class Error(val message: String) : BookingUiState()
}

class BookingViewModel(private val repository: BookingRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<BookingUiState>(BookingUiState.Loading)
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    init {
        loadBookings()
    }

    fun loadBookings() {
        viewModelScope.launch {
            _uiState.value = BookingUiState.Loading
            
            val result = repository.getBookings()
            
            result.onSuccess { bookings ->
                if (bookings.isEmpty()) {
                    _uiState.value = BookingUiState.Empty
                } else {
                    _uiState.value = BookingUiState.Success(bookings)
                }
            }.onFailure { exception ->
                _uiState.value = BookingUiState.Error(exception.message ?: "Failed to load bookings")
            }
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            repository.cancelBooking(bookingId)
            loadBookings()
        }
    }

    class Factory(private val repository: BookingRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BookingViewModel(repository) as T
        }
    }
}
