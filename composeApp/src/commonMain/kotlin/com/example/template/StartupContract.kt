package com.example.template

sealed interface StartupIntent {
    data object Initialize : StartupIntent
    data object Retry : StartupIntent
    data object AnimationFinished : StartupIntent
}

data class StartupState(
    val isLoading: Boolean = false,
    val isCompleted: Boolean = false,
    val isFinished: Boolean = false,
    val error: String? = null
)
