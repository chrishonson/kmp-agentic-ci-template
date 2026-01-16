package com.example.virtualcardexample

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val isBotTyping: Boolean = false
)

sealed interface ChatIntent {
    data class SendMessage(val sender: String, val content: String) : ChatIntent
    data object LoadMessages : ChatIntent
}

class ChatStore(
    private val chatService: ChatService,
    private val analyticsService: AnalyticsService,
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    init {
        analyticsService.logEvent("chat_session_started")
        dispatch(ChatIntent.LoadMessages)

        chatService.isBotTyping()
            .onEach { isTyping ->
                _state.update { it.copy(isBotTyping = isTyping) }
            }
            .launchIn(scope)
    }

    fun dispatch(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.SendMessage -> {
                analyticsService.logEvent("message_sent", mapOf("sender" to intent.sender))
                scope.launch {
                    chatService.sendMessage(intent.sender, intent.content)
                }
            }
            is ChatIntent.LoadMessages -> {
                chatService.getMessages()
                    .onEach { msgs ->
                        if (msgs.size > _state.value.messages.size) {
                            val lastMessage = msgs.last()
                            if (lastMessage.sender == "AWS Bot") {
                                analyticsService.logEvent("bot_response_received")
                            }
                        }
                        _state.update { it.copy(messages = msgs) }
                    }
                    .launchIn(scope)
            }
        }
    }
}
