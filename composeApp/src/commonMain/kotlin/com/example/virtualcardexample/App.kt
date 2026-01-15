package com.example.virtualcardexample

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val CARD_WIDTH_FRACTION = 0.9f
private const val CHIP_COLOR = 0xFFE0E0E0
private const val SPLASH_DELAY_MS = 2000L
private const val CROSSFADE_DURATION_MS = 1000

@Composable
@Preview
fun App() {
    AppTheme(darkTheme = true) {
        var showLanding by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            delay(SPLASH_DELAY_MS)
            showLanding = false
        }

        Crossfade(
            targetState = showLanding,
            animationSpec = tween(CROSSFADE_DURATION_MS)
        ) { landing ->
            if (landing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    HeartAnimation()
                }
            } else {
                VirtualCardScreen()
            }
        }
    }
}

@Composable
fun VirtualCardScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        VirtualCard(
            cardNumber = "**** **** **** 1234",
            cardHolder = "NIGHT SHIFT AGENT",
            expiry = "01/26",
            cvv = "999",
            isLoading = false,
            isLocked = false
        )
    }
}

@Composable
fun VirtualCard(
    cardNumber: String,
    cardHolder: String,
    expiry: String,
    cvv: String,
    isLoading: Boolean,
    isLocked: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(CARD_WIDTH_FRACTION)
            .height(220.dp)
            .testTag("CreditCard"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Gradient or Design
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
                    // Bank Name / Logo Placeholder
                    Text(
                        text = "NeoBank",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    // Chip
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(35.dp)
                            .background(
                                color = Color(CHIP_COLOR),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )

                    // Card Details
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
