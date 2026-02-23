package com.example.template

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

class StartupActionCreator(private val postService: PostService) {

    companion object {
        private const val INITIALIZATION_DELAY_MS = 100L
    }

    @Suppress("TooGenericExceptionCaught")
    suspend fun handleIntent(
        intent: StartupIntent,
        dispatch: (StartupAction) -> Unit
    ) {
        when (intent) {
            is StartupIntent.Initialize, is StartupIntent.Retry -> {
                dispatch(StartupAction.LoadingStarted)
                try {
                    delay(INITIALIZATION_DELAY_MS)
                    val post = postService.fetchPost(1)
                    dispatch(StartupAction.LoadingSucceeded(post))
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    dispatch(StartupAction.LoadingFailed(e.message ?: "Unknown error"))
                }
            }
            is StartupIntent.AnimationFinished -> {
                dispatch(StartupAction.AnimationFinished)
            }
        }
    }
}
