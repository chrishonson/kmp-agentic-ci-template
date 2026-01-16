package com.example.virtualcardexample

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val SPLASH_DELAY_MS = 2000L
private const val CROSSFADE_DURATION_MS = 1000
private const val CHAT_BUTTON_WIDTH_FRACTION = 0.8f

sealed class AppState {
    data object Splash : AppState()
    data object Login : AppState()
    data class Main(val username: String) : AppState()
    data class Chat(val username: String) : AppState()
}

 @Composable @Preview
fun App() {
    AppTheme(darkTheme = true) {
        var appState by remember { mutableStateOf<AppState>(AppState.Splash) }

        LaunchedEffect(Unit) {
            delay(SPLASH_DELAY_MS)
            appState = AppState.Login
        }

        Crossfade(
            targetState = appState,
            animationSpec = tween(CROSSFADE_DURATION_MS)
        ) { state ->
            when (state) {
                is AppState.Splash -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        HeartAnimation()
                    }
                }
                is AppState.Login -> {
                    LoginScreen(onLoginSuccess = { username ->
                        appState = AppState.Main(username)
                    })
                }
                is AppState.Main -> {
                    MainScreen(
                        username = state.username,
                        onOpenChat = { appState = AppState.Chat(state.username) }
                    )
                }
                is AppState.Chat -> {
                    val scope = rememberCoroutineScope()
                    val analyticsService = rememberAnalyticsService()
                    val chatService = remember { AwsChatService() }
                    val chatStore = remember { ChatStore(chatService, analyticsService, scope) }
                    ChatScreen(
                        store = chatStore,
                        username = state.username,
                        onBack = { appState = AppState.Main(state.username) }
                    )
                }
            }
        }
    }
}

 @Composable
fun MainScreen(
    username: String,
    onOpenChat: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hello, $username!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onOpenChat,
            modifier = Modifier.fillMaxWidth(CHAT_BUTTON_WIDTH_FRACTION)
        ) {
            Text("AWS Support Chat")
        }
    }
}
