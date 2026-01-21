package com.example.template

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class ValentineCardStore : ViewModel() {
    private val messages = listOf(
        "You are the CSS to my HTML!",
        "You're the 'git push' to my 'git commit'.",
        "My heart is a repository, and you have write access.",
        "You are the semicolon to my statements; without you, everything fails.",
        "I love you more than I love bug-free code.",
        "You're the top result in my Google search for happiness.",
        "My love for you is like an infinite loop, but with a base case: Forever."
    )

    private var currentMessageIndex = 0

    private val _state = MutableStateFlow(ValentineCardState())
    val state: StateFlow<ValentineCardState> = _state.asStateFlow()

    private fun randomBackground(): ValentineBackground {
        return ValentineBackground.entries[Random.nextInt(ValentineBackground.entries.size)]
    }

    fun dispatch(intent: ValentineCardIntent) {
        when (intent) {
            is ValentineCardIntent.UpdateRecipientName -> {
                _state.update { it.copy(recipientName = intent.name) }
            }
            ValentineCardIntent.RevealMessage -> {
                _state.update {
                    it.copy(
                        message = messages[currentMessageIndex],
                        isRevealed = true,
                        background = randomBackground()
                    )
                }
            }
            ValentineCardIntent.NextMessage -> {
                currentMessageIndex = (currentMessageIndex + 1) % messages.size
                _state.update {
                    it.copy(
                        message = messages[currentMessageIndex],
                        background = randomBackground()
                    )
                }
            }
        }
    }
}
