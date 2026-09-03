package com.example.instantmechanicassignment.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.instantmechanicassignment.domain.repository.AuthRepository
import com.example.instantmechanicassignment.model.User
import com.example.instantmechanicassignment.model.Vehicle
import com.example.instantmechanicassignment.model.PaymentMethod
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.example.instantmechanicassignment.data.local.PreferenceManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: User) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

sealed class ProfileNavigationEvent {
    object NavigateToLogin : ProfileNavigationEvent()
}

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableStateFlow<ProfileNavigationEvent?>(null)
    val navigationEvent: StateFlow<ProfileNavigationEvent?> = _navigationEvent.asStateFlow()

    private val _vehicles = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicles: StateFlow<List<Vehicle>> = _vehicles.asStateFlow()

    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethod>> = _paymentMethods.asStateFlow()

    val themeMode: StateFlow<String> = preferenceManager.getThemeMode()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "system")

    init {
        fetchProfile()
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            preferenceManager.saveThemeMode(mode)
        }
    }

    fun fetchProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            
            // Fetch from AuthRepository
            val result = authRepository.getCurrentUser()
            
            result.onSuccess { user ->
                _uiState.value = ProfileUiState.Success(user)
                loadVehicles()
                loadPaymentMethods()
            }.onFailure {
                _uiState.value = ProfileUiState.Error(it.message ?: "Failed to load profile")
            }
        }
    }

    fun loadVehicles() {
        viewModelScope.launch {
            // Simulate fetching vehicles
            delay(500)
            _vehicles.value = emptyList()
        }
    }

    fun loadPaymentMethods() {
        viewModelScope.launch {
            // Simulate fetching payment methods
            delay(500)
            _paymentMethods.value = emptyList()
        }
    }

    fun logout() {
        viewModelScope.launch {
            val result = authRepository.logout()
            result.onSuccess {
                _navigationEvent.value = ProfileNavigationEvent.NavigateToLogin
            }
        }
    }

    fun onNavigationConsumed() {
        _navigationEvent.value = null
    }

    class Factory(
        private val authRepository: AuthRepository,
        private val preferenceManager: PreferenceManager
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(authRepository, preferenceManager) as T
        }
    }
}
