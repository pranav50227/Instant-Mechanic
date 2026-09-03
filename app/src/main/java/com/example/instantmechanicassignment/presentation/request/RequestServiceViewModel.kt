package com.example.instantmechanicassignment.presentation.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instantmechanicassignment.model.Service
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class RequestServiceUiState {
    object Idle : RequestServiceUiState()
    object Loading : RequestServiceUiState()
    object Success : RequestServiceUiState()
    data class ValidationError(val message: String) : RequestServiceUiState()
    data class ApiError(val message: String) : RequestServiceUiState()
}

class RequestServiceViewModel : ViewModel() {

    private val _vehicleNumber = MutableStateFlow("")
    val vehicleNumber: StateFlow<String> = _vehicleNumber.asStateFlow()

    private val _selectedService = MutableStateFlow<Service?>(null)
    val selectedService: StateFlow<Service?> = _selectedService.asStateFlow()

    private val _date = MutableStateFlow("")
    val date: StateFlow<String> = _date.asStateFlow()

    private val _time = MutableStateFlow("")
    val time: StateFlow<String> = _time.asStateFlow()

    private val _problemDescription = MutableStateFlow("")
    val problemDescription: StateFlow<String> = _problemDescription.asStateFlow()

    private val _uiState = MutableStateFlow<RequestServiceUiState>(RequestServiceUiState.Idle)
    val uiState: StateFlow<RequestServiceUiState> = _uiState.asStateFlow()

    fun updateVehicle(vehicleNumber: String) {
        _vehicleNumber.value = vehicleNumber
    }

    fun selectService(service: Service) {
        _selectedService.value = service
    }

    fun updateDate(date: String) {
        _date.value = date
    }

    fun updateTime(time: String) {
        _time.value = time
    }

    fun updateProblem(description: String) {
        _problemDescription.value = description
    }

    fun submit(mechanicId: String) {
        if (_vehicleNumber.value.isBlank()) {
            _uiState.value = RequestServiceUiState.ValidationError("Vehicle number is required")
            return
        }
        if (_selectedService.value == null) {
            _uiState.value = RequestServiceUiState.ValidationError("Please select a service")
            return
        }
        if (_date.value.isBlank()) {
            _uiState.value = RequestServiceUiState.ValidationError("Please select a date")
            return
        }

        viewModelScope.launch {
            _uiState.value = RequestServiceUiState.Loading
            
            // Simulate repository.submitRequest(...)
            delay(2000)
            
            // Mock success
            _uiState.value = RequestServiceUiState.Success
        }
    }
}
