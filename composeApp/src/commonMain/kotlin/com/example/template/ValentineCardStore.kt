package com.example.template

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ValentineCardStore : ViewModel() {
    private val _state = MutableStateFlow(ValentineCardState())
    val state: StateFlow<ValentineCardState> = _state.asStateFlow()

    fun dispatch(intent: ValentineCardIntent) {
        when (intent) {
            ValentineCardIntent.RevealMessage -> {
                _state.update {
                    it.copy(
                        message = "You are the CSS to my HTML!",
                        isRevealed = true
                    )
                }
            }
        }
    }
}
