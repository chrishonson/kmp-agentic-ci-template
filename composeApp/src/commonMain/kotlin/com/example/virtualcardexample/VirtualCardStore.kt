package com.example.virtualcardexample

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val REFRESH_DELAY_MS = 1000L

data class VirtualCardState(
    val cardNumber: String = "**** **** **** 1234",
    val cardHolder: String = "NIGHT SHIFT AGENT",
    val expiry: String = "01/26",
    val cvv: String = "999",
    val isLoading: Boolean = false,
    val isLocked: Boolean = false
)

sealed interface VirtualCardIntent {
    data object ToggleLock : VirtualCardIntent
    data object Refresh : VirtualCardIntent
}

class VirtualCardStore(private val scope: CoroutineScope) {
    private val _state = MutableStateFlow(VirtualCardState())
    val state: StateFlow<VirtualCardState> = _state.asStateFlow()

    fun dispatch(intent: VirtualCardIntent) {
        when (intent) {
            is VirtualCardIntent.ToggleLock -> {
                _state.update { it.copy(isLocked = !it.isLocked) }
            }
            is VirtualCardIntent.Refresh -> {
                scope.launch {
                    _state.update { it.copy(isLoading = true) }
                    // Simulate network delay
                    kotlinx.coroutines.delay(REFRESH_DELAY_MS)
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}
