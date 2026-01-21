package com.example.template

enum class ValentineBackground {
    HEARTS_FLOATING,
    LOVE_GRADIENT,
    CUPID_ARROWS,
    ROSES_PATTERN,
    SPARKLE_PINK,
    CANDY_HEARTS,
    ROMANTIC_SUNSET,
    LOVE_LETTERS
}

data class ValentineCardState(
    val recipientName: String = "",
    val message: String = "",
    val isRevealed: Boolean = false,
    val background: ValentineBackground = ValentineBackground.HEARTS_FLOATING
)

sealed interface ValentineCardIntent {
    data class UpdateRecipientName(val name: String) : ValentineCardIntent
    data object RevealMessage : ValentineCardIntent
    data object NextMessage : ValentineCardIntent
}
