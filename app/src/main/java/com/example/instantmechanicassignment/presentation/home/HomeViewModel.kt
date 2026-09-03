package com.example.instantmechanicassignment.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.instantmechanicassignment.domain.repository.MechanicRepository
import com.example.instantmechanicassignment.model.Mechanic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val mechanics: List<Mechanic>) : HomeUiState()
    object Empty : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(private val repository: MechanicRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var allMechanics = listOf<Mechanic>()

    init {
        loadMechanics()
    }

    fun loadMechanics() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            
            val result = repository.getMechanics()
            
            result.onSuccess { mechanics ->
                allMechanics = mechanics
                if (allMechanics.isEmpty()) {
                    _uiState.value = HomeUiState.Empty
                } else {
                    _uiState.value = HomeUiState.Success(allMechanics)
                }
            }.onFailure { exception ->
                _uiState.value = HomeUiState.Error(exception.message ?: "Failed to load mechanics")
            }
        }
    }

    fun search(query: String) {
        if (query.isEmpty()) {
            _uiState.value = HomeUiState.Success(allMechanics)
            return
        }
        
        viewModelScope.launch {
            val result = repository.searchMechanics(query)
            result.onSuccess { filtered ->
                if (filtered.isEmpty()) {
                    _uiState.value = HomeUiState.Empty
                } else {
                    _uiState.value = HomeUiState.Success(filtered)
                }
            }
        }
    }

    fun filter(serviceName: String) {
        viewModelScope.launch {
            val result = repository.filterMechanics(serviceName)
            result.onSuccess { filtered ->
                if (filtered.isEmpty()) {
                    _uiState.value = HomeUiState.Empty
                } else {
                    _uiState.value = HomeUiState.Success(filtered)
                }
            }
        }
    }

    fun refresh() {
        loadMechanics()
    }

    class Factory(private val repository: MechanicRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repository) as T
        }
    }
}
