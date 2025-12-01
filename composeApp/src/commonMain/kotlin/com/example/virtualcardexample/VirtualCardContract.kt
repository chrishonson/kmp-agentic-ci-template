package com.example.virtualcardexample

data class VirtualCardState(
    val cardNumber: String = "**** **** **** 3456",
    val cardHolder: String = "Nick Antigravity",
    val expiry: String = "**/**",
    val cvv: String = "***",
    val isRevealed: Boolean = false,
    val buttonText: String = "Reveal Details"
)

sealed interface VirtualCardIntent {
    data object ToggleVisibility : VirtualCardIntent
}
