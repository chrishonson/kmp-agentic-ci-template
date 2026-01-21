package com.example.template

import androidx.compose.runtime.Immutable

@Immutable
data class StartupState(
    val isFinished: Boolean = false
)

sealed interface StartupIntent {
    data object AnimationFinished : StartupIntent
}
