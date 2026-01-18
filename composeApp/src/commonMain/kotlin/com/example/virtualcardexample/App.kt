package com.example.virtualcardexample

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

 @Composable
fun App() {
    // Define a dark color scheme
    val darkColorScheme = darkColorScheme(
        primary = Color(0xFFBB86FC),
        secondary = Color(0xFF03DAC5),
        tertiary = Color(0xFF3700B3),
        background = Color(0xFF121212), // Dark background
        surface = Color(0xFF121212), // Dark surface
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color.White,
        onSurface = Color.White
    )

    MaterialTheme(colorScheme = darkColorScheme) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { // Apply dark background
            ValentineCard(recipientName = "Valentine", message = "Happy Valentine's Day!")
        }
    }
}

 @Composable
fun ValentineCard(recipientName: String, message: String) {
    // Define gradient colors for the background
    val gradientColors = listOf(
        Color(0xFFE91E63), // Pink
        Color(0xFF9C27B0)  // Purple
    )

    // Create a brush for the gradient
    val gradientBrush = Brush.linearGradient(colors = gradientColors)

    // Animated heart properties
    val infiniteTransition = rememberInfiniteTransition()
    val heartSize by infiniteTransition.animateFloat(
        initialValue = 40f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Animated card rotation
    val rotationState = remember { mutableStateOf(0f) }

    LaunchedEffect(key1 = true) {
        while (true) {
            rotationState.value = (rotationState.value + 5f) % 360f
            delay(50)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // Apply dark background
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .size(300.dp)
                .clip(RoundedCornerShape(20.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradientBrush)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Heart
                Box(contentAlignment = Alignment.Center) {
                    Text("❤️", fontSize = heartSize.dp, modifier = Modifier.testTag("Heart"), color = Color.White)
                }

                // Message Content
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = "Dear $recipientName,",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.testTag("RecipientName")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("ValentineMessage")
                    )
                }

                Text(
                    text = "With Love",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}