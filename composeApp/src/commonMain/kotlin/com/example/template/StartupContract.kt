package com.example.template

// -- Intents: What the UI sends (user-facing) --

sealed interface StartupIntent {
    data object Initialize : StartupIntent
    data object Retry : StartupIntent
    data object AnimationFinished : StartupIntent
}

// -- Actions: What the reducer processes (internal) --

sealed interface StartupAction {
    data object LoadingStarted : StartupAction
    data class LoadingSucceeded(val post: Post) : StartupAction
    data class LoadingFailed(val error: String) : StartupAction
    data object AnimationFinished : StartupAction
}

// -- State --

data class StartupState(
    val isLoading: Boolean = false,
    val isCompleted: Boolean = false,
    val isFinished: Boolean = false,
    val post: Post? = null,
    val error: String? = null
)

// -- Reducer: Pure function, no side effects --

fun startupReducer(state: StartupState, action: StartupAction): StartupState {
    return when (action) {
        is StartupAction.LoadingStarted ->
            state.copy(isLoading = true, error = null)
        is StartupAction.LoadingSucceeded ->
            state.copy(isLoading = false, isCompleted = true, post = action.post)
        is StartupAction.LoadingFailed ->
            state.copy(isLoading = false, error = action.error)
        is StartupAction.AnimationFinished ->
            state.copy(isFinished = true)
    }
}
