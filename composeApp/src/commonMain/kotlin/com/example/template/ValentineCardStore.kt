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
    private val actionCreator = ValentineCardActionCreator(
        messages = messages.ifEmpty { defaultMessages }
    )
    private val _state = MutableStateFlow(
        ValentineCardState(
            message = actionCreator.currentMessage(),
            background = background
        )
    )
    val state: StateFlow<ValentineCardState> = _state

    private fun reduce(action: ValentineCardAction) {
        _state.value = valentineCardReducer(_state.value, action)
    }

    fun dispatch(intent: ValentineCardIntent) {
        actionCreator.handleIntent(intent, ::reduce)
    }
}
