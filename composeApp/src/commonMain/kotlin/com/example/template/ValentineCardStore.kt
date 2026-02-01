package com.example.template

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private val defaultMessages = listOf(
    "You make every day feel special.",
    "Happy Valentine's Day!",
    "You are my favorite hello."
)

class ValentineCardStore(
    messages: List<String> = defaultMessages,
    background: ValentineBackground = ValentineBackground.HEARTS_FLOATING
) : ViewModel() {
    private val messageCycle = if (messages.isNotEmpty()) messages else defaultMessages
    private var currentMessageIndex = 0
    private val _state = MutableStateFlow(
        ValentineCardState(
        message = messageCycle.first(),
        background = background
    )
    )
    val state: StateFlow<ValentineCardState> = _state

    fun dispatch(intent: ValentineCardIntent) {
        when (intent) {
            is ValentineCardIntent.UpdateRecipientName -> {
                _state.value = _state.value.copy(recipientName = intent.name)
            }
            ValentineCardIntent.RevealMessage -> {
                _state.value = _state.value.copy(isRevealed = true)
            }
            ValentineCardIntent.NextMessage -> {
                currentMessageIndex = (currentMessageIndex + 1) % messageCycle.size
                _state.value = _state.value.copy(message = messageCycle[currentMessageIndex])
            }
        }
    }
}
