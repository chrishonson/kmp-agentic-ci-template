package com.example.virtualcardexample

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ChatMessage(
    val id: String,
    val sender: String,
    val content: String,
    val timestamp: String
)

interface ChatService {
    fun getMessages(): Flow<List<ChatMessage>>
    suspend fun sendMessage(sender: String, content: String)
}

class ChatServiceStub : ChatService {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())

    override fun getMessages(): Flow<List<ChatMessage>> = _messages.asStateFlow()

    override suspend fun sendMessage(sender: String, content: String) {
        val time = getCurrentTimestamp()
        val newMessage = ChatMessage(
            id = time + "_" + sender,
            sender = sender,
            content = content,
            timestamp = time
        )
        _messages.update { it + newMessage }

        // Simulate AWS response
        if (content.lowercase().contains("hello")) {
            val response = ChatMessage(
                id = getCurrentTimestamp() + "_bot",
                sender = "AWS Bot",
                content = "Hello! I am your AWS-powered assistant. How can I help you today?",
                timestamp = getCurrentTimestamp()
            )
            _messages.update { it + response }
        }
    }
}
