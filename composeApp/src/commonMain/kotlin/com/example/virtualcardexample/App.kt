package com.example.virtualcardexample

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val CARD_WIDTH_FRACTION = 0.9f
private const val CHIP_COLOR = 0xFFE0E0E0
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
                    val scope = rememberCoroutineScope()
                    val store = remember { VirtualCardStore(scope) }
                    VirtualCardScreen(
                        store = store,
                        username = state.username,
                        onOpenChat = { appState = AppState.Chat(state.username) }
                    )
                }
                is AppState.Chat -> {
                    val scope = rememberCoroutineScope()
                    val analyticsService = remember { ConsoleAnalyticsService() }
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
fun VirtualCardScreen(
    store: VirtualCardStore,
    username: String,
    onOpenChat: () -> Unit
) {
    val state by store.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$username, will you be my valentine?",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp),
            textAlign = TextAlign.Center
        )

        VirtualCard(
            cardNumber = state.cardNumber,
            cardHolder = state.cardHolder,
            expiry = state.expiry,
            cvv = state.cvv,
            isLoading = state.isLoading,
            isLocked = state.isLocked,
            onToggleLock = { store.dispatch(VirtualCardIntent.ToggleLock) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onOpenChat,
            modifier = Modifier.fillMaxWidth(CHAT_BUTTON_WIDTH_FRACTION)
        ) {
            Text("Chat with me")
        }
    }
}

 @Composable
fun VirtualCard(
    cardNumber: String,
    cardHolder: String,
    expiry: String,
    cvv: String,
    isLoading: Boolean,
    isLocked: Boolean,
    onToggleLock: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(CARD_WIDTH_FRACTION)
            .height(220.dp)
            .clickable { onToggleLock() }
            .testTag("CreditCard"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        )
                    )
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.semantics { contentDescription = "Loading" }
                    )
                }
            } else if (isLocked) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CARD LOCKED",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "be my valentine",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(35.dp)
                            .background(
                                color = Color(CHIP_COLOR),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )

                    Column {
                        Text(
                            text = cardNumber,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.testTag("CardNumber")
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "CARD HOLDER",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = cardHolder,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                            }

                            Column {
                                Text(
                                    text = "EXPIRES",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = expiry,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                            }

                            Column {
                                Text(
                                    text = "CVV",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = cvv,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
