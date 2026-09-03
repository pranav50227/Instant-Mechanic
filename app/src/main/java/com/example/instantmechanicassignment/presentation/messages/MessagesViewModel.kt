package com.example.instantmechanicassignment.presentation.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instantmechanicassignment.model.Chat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MessagesUiState {
    object Loading : MessagesUiState()
    data class Success(val chats: List<Chat>) : MessagesUiState()
    object Empty : MessagesUiState()
    data class Error(val message: String) : MessagesUiState()
}

class MessagesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<MessagesUiState>(MessagesUiState.Loading)
    val uiState: StateFlow<MessagesUiState> = _uiState.asStateFlow()

    init {
        loadConversations()
    }

    fun loadConversations() {
        viewModelScope.launch {
            _uiState.value = MessagesUiState.Loading
            
            // Simulate repository.getChats()
            delay(1500)
            
            // Mock empty state for now
            _uiState.value = MessagesUiState.Empty
        }
    }

    fun refreshMessages() {
        loadConversations()
    }
}
