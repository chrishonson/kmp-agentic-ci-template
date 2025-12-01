package com.example.virtualcardexample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        VirtualCardScreen()
    }
}

@Composable
fun VirtualCardScreen() {
    val store = androidx.lifecycle.viewmodel.compose.viewModel { VirtualCardStore() }
    val state by store.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        VirtualCard(state = state)
        
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { store.dispatch(VirtualCardIntent.ToggleVisibility) },
            modifier = Modifier.testTag("RevealButton")
        ) {
            Text(state.buttonText)
        }
    }
}

@Composable
fun VirtualCard(state: VirtualCardState) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
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
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                // Bank Name / Logo Placeholder
                Text(
                    text = "NeoBank",
                    style = MaterialTheme.typography.titleLarge,
                    color = androidx.compose.ui.graphics.Color.White,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )

                // Chip
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(35.dp)
                        .background(
                            color = androidx.compose.ui.graphics.Color(0xFFE0E0E0),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                        )
                )

                // Card Details
                Column {
                    Text(
                        text = state.cardNumber,
                        style = MaterialTheme.typography.headlineMedium,
                        color = androidx.compose.ui.graphics.Color.White,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        modifier = Modifier.testTag("CardNumber")
                    )
                    
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
                    
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "CARD HOLDER",
                                style = MaterialTheme.typography.labelSmall,
                                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = state.cardHolder,
                                style = MaterialTheme.typography.bodyMedium,
                                color = androidx.compose.ui.graphics.Color.White
                            )
                        }
                        
                        Column {
                            Text(
                                text = "EXPIRES",
                                style = MaterialTheme.typography.labelSmall,
                                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = state.expiry,
                                style = MaterialTheme.typography.bodyMedium,
                                color = androidx.compose.ui.graphics.Color.White
                            )
                        }
                        
                         Column {
                            Text(
                                text = "CVV",
                                style = MaterialTheme.typography.labelSmall,
                                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = state.cvv,
                                style = MaterialTheme.typography.bodyMedium,
                                color = androidx.compose.ui.graphics.Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}