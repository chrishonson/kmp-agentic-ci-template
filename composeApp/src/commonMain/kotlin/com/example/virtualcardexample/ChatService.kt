package com.example.virtualcardexample

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val BOT_RESPONSE_DELAY_MS = 500L

data class ChatMessage(
    val id: String,
    val sender: String,
    val content: String,
    val timestamp: String
)

/**
 * Interface for the Amazon Lex Runtime service.
 * Assumed to be provided by the AWS SDK.
 */
interface LexRuntimeService {
    suspend fun postText(text: String): String
}

/**
 * Interface for the AWS AppSync service for real-time data.
 * Assumed to be provided by the AWS SDK / Amplify.
 */
interface AppSyncService {
    fun subscribeToMessages(): Flow<List<ChatMessage>>
    suspend fun publishMessage(message: ChatMessage)
}

/**
 * Interface for chat services.
 */
interface ChatService {
    fun getMessages(): Flow<List<ChatMessage>>
    fun isBotTyping(): Flow<Boolean>
    suspend fun sendMessage(sender: String, content: String)
}

/**
 * Implementation of ChatService assuming AWS backend services are available.
 */
class AwsChatService(
    private val lexRuntime: LexRuntimeService = LexRuntimeStub(),
    private val appSync: AppSyncService = AppSyncStub()
) : ChatService {

    private val _isBotTyping = MutableStateFlow(false)
    override fun isBotTyping(): Flow<Boolean> = _isBotTyping.asStateFlow()

    override fun getMessages(): Flow<List<ChatMessage>> = appSync.subscribeToMessages()

    override suspend fun sendMessage(sender: String, content: String) {
        val time = getCurrentTimestamp()
        val newMessage = ChatMessage(
            id = time + "_" + sender,
            sender = sender,
            content = content,
            timestamp = time
        )

        // Publish to AppSync (assumed to sync with DynamoDB)
        appSync.publishMessage(newMessage)

        // Simulate Lex Bot processing delay
        _isBotTyping.value = true
        delay(BOT_RESPONSE_DELAY_MS)
        
        val botResponseText = lexRuntime.postText(content)
        _isBotTyping.value = false

        if (botResponseText.isNotEmpty()) {
            val botResponse = ChatMessage(
                id = getCurrentTimestamp() + "_bot",
                sender = "AWS Bot",
                content = botResponseText,
                timestamp = getCurrentTimestamp()
            )
            appSync.publishMessage(botResponse)
        }
    }
}

private class LexRuntimeStub : LexRuntimeService {
    override suspend fun postText(text: String): String {
        return when {
            text.lowercase().contains("hello") -> "Hello! I am your AWS-powered assistant. How can I help you today?"
            text.lowercase().contains("balance") -> "Your current virtual card balance is $500.00."
            text.lowercase().contains("lock") -> "I can help you lock your card. Would you like me to proceed?"
            else -> "I received your message: \"$text\". How else can I assist you with your virtual card?"
        }
    }
}

private class AppSyncStub : AppSyncService {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())

    override fun subscribeToMessages(): Flow<List<ChatMessage>> = _messages.asStateFlow()

    override suspend fun publishMessage(message: ChatMessage) {
        _messages.update { it + message }
    }
}
