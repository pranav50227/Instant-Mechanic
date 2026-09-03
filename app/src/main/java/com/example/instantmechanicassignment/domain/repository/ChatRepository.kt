package com.example.instantmechanicassignment.domain.repository

import com.example.instantmechanicassignment.model.Chat
import com.example.instantmechanicassignment.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChats(): Result<List<Chat>>
    fun getMessages(chatId: String): Flow<List<Message>>
    suspend fun sendMessage(chatId: String, text: String, receiverId: String): Result<Unit>
    suspend fun markAsRead(chatId: String): Result<Unit>
}
