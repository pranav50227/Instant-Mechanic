package com.example.instantmechanicassignment.data.repository

import com.example.instantmechanicassignment.data.mock.MockChatData
import com.example.instantmechanicassignment.data.remote.ApiService
import com.example.instantmechanicassignment.domain.repository.ChatRepository
import com.example.instantmechanicassignment.model.Chat
import com.example.instantmechanicassignment.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ChatRepositoryImpl(
    private val apiService: ApiService
) : ChatRepository {

    private val _messages = MutableStateFlow<Map<String, List<Message>>>(MockChatData.messages)

    override suspend fun getChats(): Result<List<Chat>> = withContext(Dispatchers.IO) {
        try {
            delay(1000)
            Result.success(MockChatData.chats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getMessages(chatId: String): Flow<List<Message>> {
        return _messages.map { it[chatId] ?: emptyList() }
    }

    override suspend fun sendMessage(chatId: String, text: String, receiverId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            delay(500)
            val currentMap = _messages.value.toMutableMap()
            val chatMessages = currentMap[chatId]?.toMutableList() ?: mutableListOf()
            
            chatMessages.add(Message(
                messageId = "M${System.currentTimeMillis()}",
                chatId = chatId,
                senderId = "U101", // Match MockAuthData.mockUser.userId
                receiverId = receiverId,
                message = text,
                sentTime = "Just now",
                messageType = "text",
                isRead = false
            ))
            
            currentMap[chatId] = chatMessages
            _messages.value = currentMap
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAsRead(chatId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            delay(200)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
