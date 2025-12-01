package com.example.virtualcardexample

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VirtualCardStore : ViewModel() {

    private val _state = MutableStateFlow(VirtualCardState())
    val state: StateFlow<VirtualCardState> = _state.asStateFlow()

    fun dispatch(intent: VirtualCardIntent) {
        when (intent) {
            VirtualCardIntent.ToggleVisibility -> toggleVisibility()
        }
    }

    private fun toggleVisibility() {
        _state.update { currentState ->
            val newRevealed = !currentState.isRevealed
            currentState.copy(
                isRevealed = newRevealed,
                cardNumber = if (newRevealed) "1234 5678 9012 3456" else "**** **** **** 3456",
                expiry = if (newRevealed) "12/28" else "**/**",
                cvv = if (newRevealed) "123" else "***",
                buttonText = if (newRevealed) "Hide Details" else "Reveal Details"
            )
        }
    }
}
