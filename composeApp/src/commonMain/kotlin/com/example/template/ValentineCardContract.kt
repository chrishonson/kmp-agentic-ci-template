package com.example.template

data class ValentineCardState(
    val message: String = "",
    val isRevealed: Boolean = false
)

sealed interface ValentineCardIntent {
    data object RevealMessage : ValentineCardIntent
}
