package com.example.template

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PRIMARY_PINK = Color(0xFFE91E63)
private val SECONDARY_PINK = Color(0xFFFCE4EC)
private val DARK_PINK = Color(0xFF880E4F)
private val PADDING_16 = 16.dp
private val PADDING_8 = 8.dp
private val PADDING_24 = 24.dp
private val SPACING_32 = 32.dp
private val SPACING_8 = 8.dp
private val FONT_SIZE_20 = 20.sp

@Composable
fun ValentineCardScreen(store: ValentineCardStore) {
    val state by store.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(PADDING_16),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Happy Valentine's Day!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = PRIMARY_PINK
            )

            Spacer(modifier = Modifier.height(SPACING_32))

            AnimatedVisibility(
                visible = !state.isRevealed,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Button(
                    onClick = { store.dispatch(ValentineCardIntent.RevealMessage) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PRIMARY_PINK
                    )
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null)
                    Spacer(Modifier.width(SPACING_8))
                    Text("Reveal Message")
                }
            }

            AnimatedVisibility(
                visible = state.isRevealed,
                enter = fadeIn() + expandVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PADDING_8),
                    colors = CardDefaults.cardColors(
                        containerColor = SECONDARY_PINK
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(PADDING_24),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = DARK_PINK,
                            fontWeight = FontWeight.Medium,
                            fontSize = FONT_SIZE_20
                        )
                    }
                }
            }
        }
    }
}
