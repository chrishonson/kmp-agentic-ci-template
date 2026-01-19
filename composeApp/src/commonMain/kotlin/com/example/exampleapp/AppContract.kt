package com.example.exampleapp

data class AppState(
    val cardNumber: String = "**** **** **** 3456",
    val cardHolder: String = "Nick Antigravity",
    val expiry: String = "**/**",
    val cvv: String = "***",
    val isRevealed: Boolean = false,
    val isLocked: Boolean = false,
    val buttonText: String = "Reveal Details",
    val isLoading: Boolean = false,
    val loadingMessage: String? = null
)

sealed interface AppIntent {
    data object ToggleVisibility : AppIntent
    data object ToggleLock : AppIntent
    data object LoadCardDetails : AppIntent
    data object ReplaceCard : AppIntent
}

// Data class to represent the fetched card details
data class CardDetails(
    val cardNumber: String,
    val cardHolder: String,
    val expiry: String,
    val cvv: String
)
