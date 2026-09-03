package com.example.instantmechanicassignment.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.instantmechanicassignment.domain.repository.AuthRepository
import com.example.instantmechanicassignment.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    // Enable/disable Login button based on validation
    val isLoginEnabled: StateFlow<Boolean> = combine(_email, _password, _loginState) { email, password, state ->
        val isEmailValid = email.isNotEmpty()
        val isPasswordValid = password.isNotEmpty() && password.length >= 8
        isEmailValid && isPasswordValid && state !is LoginState.Loading
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        // Reset error state when user starts typing again
        if (_loginState.value is LoginState.Error) {
            _loginState.value = LoginState.Idle
        }
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
        // Reset error state when user starts typing again
        if (_loginState.value is LoginState.Error) {
            _loginState.value = LoginState.Idle
        }
    }

    fun login() {
        val emailValue = _email.value
        val passwordValue = _password.value

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            
            val result = authRepository.login(emailValue, passwordValue)
            
            result.onSuccess { user ->
                _loginState.value = LoginState.Success(user)
            }.onFailure { exception ->
                _loginState.value = LoginState.Error(exception.message ?: "Login failed")
            }
        }
    }

    class Factory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(authRepository) as T
        }
    }
}
