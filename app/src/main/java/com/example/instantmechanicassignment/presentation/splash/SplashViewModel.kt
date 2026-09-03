package com.example.instantmechanicassignment.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.instantmechanicassignment.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SplashState {
    object Loading : SplashState()
    object NavigateToLogin : SplashState()
    object NavigateToHome : SplashState()
    data class Error(val message: String) : SplashState()
}

class SplashViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow<SplashState>(SplashState.Loading)
    val state: StateFlow<SplashState> = _state.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            // Simulate session check delay
            delay(1500)
            
            try {
                val token = authRepository.getToken()
                if (token != null) {
                    _state.value = SplashState.NavigateToHome
                } else {
                    _state.value = SplashState.NavigateToLogin
                }
            } catch (e: Exception) {
                _state.value = SplashState.NavigateToLogin
            }
        }
    }

    class Factory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SplashViewModel(authRepository) as T
        }
    }
}
