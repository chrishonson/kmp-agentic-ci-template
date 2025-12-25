package com.example.virtualcardexample

import androidx.compose.runtime.Immutable

@Immutable
data class VirtualCardState(
    val cardNumber: String = "**** **** **** ****",
    val cardHolder: String = "---- ----",
    val expiry: String = "**/**",
    val cvv: String = "***",
    val isRevealed: Boolean = false,
    val isLocked: Boolean = false,
    val isLoading: Boolean = false,
    val loadingMessage: String? = null,
    val buttonText: String = "Reveal Details",
    val networkResponse: String? = null
)

sealed interface VirtualCardIntent {
    data object ToggleVisibility : VirtualCardIntent
    data object LoadCardDetails : VirtualCardIntent
    data object ToggleLock : VirtualCardIntent
    data object ReplaceCard : VirtualCardIntent
    data object TestNetworkCall : VirtualCardIntent
}

data class CardDetails(
    val cardNumber: String,
    val cardHolder: String,
    val expiry: String,
    val cvv: String
)
