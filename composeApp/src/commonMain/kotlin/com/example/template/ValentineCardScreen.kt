package com.example.template

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PADDING_16 = 16.dp
private val PADDING_8 = 8.dp
private val PADDING_24 = 24.dp
private val SPACING_32 = 32.dp
private val SPACING_16 = 16.dp
private val SPACING_8 = 8.dp
private val FONT_SIZE_20 = 20.sp

@Composable
fun ValentineCardScreen(store: ValentineCardStore) {
    val state by store.state.collectAsState()

    ValentineCardContent(
        recipientName = state.recipientName,
        message = state.message,
        isRevealed = state.isRevealed,
        onNameChange = { store.dispatch(ValentineCardIntent.UpdateRecipientName(it)) },
        onReveal = { store.dispatch(ValentineCardIntent.RevealMessage) },
        onNextMessage = { store.dispatch(ValentineCardIntent.NextMessage) }
    )
}

@Composable
fun ValentineCardContent(
    recipientName: String,
    message: String,
    isRevealed: Boolean,
    onNameChange: (String) -> Unit,
    onReveal: () -> Unit,
    onNextMessage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(SPACING_32))

            AnimatedVisibility(
                visible = !isRevealed,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = recipientName,
                        onValueChange = onNameChange,
                        label = { Text("Who is it for?") },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = PADDING_8),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(SPACING_16))

                    Button(
                        onClick = onReveal,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null)
                        Spacer(Modifier.width(SPACING_8))
                        Text("Reveal Message")
                    }
                }
            }

            AnimatedVisibility(
                visible = isRevealed,
                enter = fadeIn() + expandVertically()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (recipientName.isNotBlank()) {
                        Text(
                            text = "To: $recipientName",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(SPACING_8))
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(PADDING_8),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(PADDING_24),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontWeight = FontWeight.Medium,
                                fontSize = FONT_SIZE_20
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(SPACING_16))

                    Button(
                        onClick = onNextMessage,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(Modifier.width(SPACING_8))
                        Text("Another One!")
                    }
                }
            }
        }
    }
}
