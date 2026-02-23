package com.example.template

class ValentineCardActionCreator(messages: List<String>) {
    private val messageCycle = messages
    private var currentMessageIndex = 0

    fun currentMessage(): String = messageCycle[currentMessageIndex]

    fun handleIntent(
        intent: ValentineCardIntent,
        dispatch: (ValentineCardAction) -> Unit
    ) {
        when (intent) {
            is ValentineCardIntent.UpdateRecipientName ->
                dispatch(ValentineCardAction.UpdateRecipientName(intent.name))
            is ValentineCardIntent.RevealMessage ->
                dispatch(ValentineCardAction.Reveal)
            is ValentineCardIntent.NextMessage -> {
                currentMessageIndex = (currentMessageIndex + 1) % messageCycle.size
                dispatch(ValentineCardAction.SetMessage(messageCycle[currentMessageIndex]))
            }
        }
    }
}
