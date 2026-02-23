package com.example.template

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StartupStore(private val actionCreator: StartupActionCreator) : ViewModel() {
    private val _state = MutableStateFlow(StartupState())
    val state: StateFlow<StartupState> = _state.asStateFlow()

    private fun reduce(action: StartupAction) {
        _state.update { startupReducer(it, action) }
    }

    fun dispatch(intent: StartupIntent) {
        viewModelScope.launch {
            actionCreator.handleIntent(intent, ::reduce)
        }
    }
}
