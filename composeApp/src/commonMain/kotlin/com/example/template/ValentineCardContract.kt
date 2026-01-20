package com.example.template

data class ValentineCardState(
    val recipientName: String = "",
    val message: String = "",
    val isRevealed: Boolean = false
)

sealed interface ValentineCardIntent {
    data class UpdateRecipientName(val name: String) : ValentineCardIntent
    data object RevealMessage : ValentineCardIntent
    data object NextMessage : ValentineCardIntent
}
