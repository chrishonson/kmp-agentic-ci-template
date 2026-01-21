package com.example.template

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StartupStore : ViewModel() {
    private val _state = MutableStateFlow(StartupState())
    val state: StateFlow<StartupState> = _state.asStateFlow()

    fun dispatch(intent: StartupIntent) {
        when (intent) {
            StartupIntent.AnimationFinished -> {
                _state.update { it.copy(isFinished = true) }
            }
        }
    }
}
