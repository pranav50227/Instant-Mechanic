package com.example.instantmechanicassignment.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.instantmechanicassignment.domain.repository.AuthRepository
import com.example.instantmechanicassignment.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class SignupState {
    object Idle : SignupState()
    object Loading : SignupState()
    data class Success(val user: User) : SignupState()
    data class ValidationError(val message: String) : SignupState()
    data class ApiError(val message: String) : SignupState()
}

class SignupViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _city = MutableStateFlow("")
    val city: StateFlow<String> = _city.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _signupState = MutableStateFlow<SignupState>(SignupState.Idle)
    val signupState: StateFlow<SignupState> = _signupState.asStateFlow()

    val isSignupEnabled: StateFlow<Boolean> = combine(
        _fullName, _email, _phoneNumber, _city, _password, _signupState
    ) { args: Array<Any> ->
        val name = args[0] as String
        val email = args[1] as String
        val phone = args[2] as String
        val city = args[3] as String
        val pass = args[4] as String
        val state = args[5] as SignupState

        val isValid = name.isNotBlank() &&
                email.contains("@") &&
                phone.length >= 10 &&
                city.isNotBlank() &&
                pass.length >= 6
        isValid && state !is SignupState.Loading
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun updateName(name: String) {
        _fullName.value = name
    }

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updatePhone(phone: String) {
        _phoneNumber.value = phone
    }

    fun updateCity(city: String) {
        _city.value = city
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    fun register() {
        val name = _fullName.value
        val emailValue = _email.value
        val phone = _phoneNumber.value
        val cityValue = _city.value
        val pass = _password.value

        viewModelScope.launch {
            _signupState.value = SignupState.Loading
            
            val result = authRepository.signup(
                fullName = name,
                email = emailValue,
                phoneNumber = phone,
                city = cityValue,
                password = pass
            )
            
            result.onSuccess { user ->
                _signupState.value = SignupState.Success(user)
            }.onFailure { exception ->
                _signupState.value = SignupState.ApiError(exception.message ?: "Signup failed")
            }
        }
    }

    class Factory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SignupViewModel(authRepository) as T
        }
    }
}
