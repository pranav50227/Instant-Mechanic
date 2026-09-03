package com.example.instantmechanicassignment.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instantmechanicassignment.model.Mechanic
import androidx.lifecycle.ViewModelProvider
import com.example.instantmechanicassignment.domain.repository.MechanicRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MechanicDetailsUiState {
    object Loading : MechanicDetailsUiState()
    data class Success(val mechanic: Mechanic) : MechanicDetailsUiState()
    data class Error(val message: String) : MechanicDetailsUiState()
}

class MechanicDetailsViewModel(private val repository: MechanicRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<MechanicDetailsUiState>(MechanicDetailsUiState.Loading)
    val uiState: StateFlow<MechanicDetailsUiState> = _uiState.asStateFlow()

    fun loadMechanic(mechanicId: String) {
        viewModelScope.launch {
            _uiState.value = MechanicDetailsUiState.Loading
            
            val result = repository.getMechanicDetails(mechanicId)
            
            result.onSuccess { mechanic ->
                _uiState.value = MechanicDetailsUiState.Success(mechanic)
            }.onFailure { exception ->
                _uiState.value = MechanicDetailsUiState.Error(exception.message ?: "Failed to load details")
            }
        }
    }

    fun retry(mechanicId: String) {
        loadMechanic(mechanicId)
    }

    fun handleCall(phoneNumber: String) {
        // Implementation for opening dialer
    }

    fun handleDirections(latitude: Double, longitude: Double) {
        // Implementation for opening maps
    }

    class Factory(private val repository: MechanicRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MechanicDetailsViewModel(repository) as T
        }
    }
}
