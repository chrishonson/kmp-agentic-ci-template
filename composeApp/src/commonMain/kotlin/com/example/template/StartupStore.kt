package com.example.template

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StartupStore : ViewModel() {
    private val _state = MutableStateFlow(StartupState())
    val state: StateFlow<StartupState> = _state.asStateFlow()

    companion object {
        private const val INITIALIZATION_DELAY_MS = 100L
    }

    fun dispatch(intent: StartupIntent) {
        when (intent) {
            is StartupIntent.Initialize -> {
                performInitialization()
            }
            is StartupIntent.Retry -> {
                performInitialization()
            }
            is StartupIntent.AnimationFinished -> {
                _state.update { it.copy(isFinished = true) }
            }
        }
    }

    private fun performInitialization() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            // Simulate initialization work
            delay(INITIALIZATION_DELAY_MS)
            _state.update { it.copy(isLoading = false, isCompleted = true) }
        }
    }
}
