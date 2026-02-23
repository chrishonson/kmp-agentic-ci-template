package com.example.template

// -- Backgrounds --

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

// -- Intents: What the UI sends (user-facing) --

sealed interface ValentineCardIntent {
    data class UpdateRecipientName(val name: String) : ValentineCardIntent
    data object RevealMessage : ValentineCardIntent
    data object NextMessage : ValentineCardIntent
}

// -- Actions: What the reducer processes (internal) --

sealed interface ValentineCardAction {
    data class UpdateRecipientName(val name: String) : ValentineCardAction
    data object Reveal : ValentineCardAction
    data class SetMessage(val message: String) : ValentineCardAction
}

// -- State --

data class ValentineCardState(
    val recipientName: String = "",
    val message: String = "",
    val isRevealed: Boolean = false,
    val background: ValentineBackground = ValentineBackground.HEARTS_FLOATING
)

// -- Reducer: Pure function, no side effects --

fun valentineCardReducer(
    state: ValentineCardState,
    action: ValentineCardAction
): ValentineCardState {
    return when (action) {
        is ValentineCardAction.UpdateRecipientName ->
            state.copy(recipientName = action.name)
        is ValentineCardAction.Reveal ->
            state.copy(isRevealed = true)
        is ValentineCardAction.SetMessage ->
            state.copy(message = action.message)
    }
}
