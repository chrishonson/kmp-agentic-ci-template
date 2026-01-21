package com.example.template

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

 @Composable @Preview
fun App() {
    val systemDark = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(systemDark) }

    AppTheme(darkTheme = isDarkTheme) {
        var showStartup by remember { mutableStateOf(true) }

        Box(modifier = Modifier.fillMaxSize()) {
            if (showStartup) {
                val store: StartupStore = viewModel { StartupStore() }
                StartupScreen(
                    store = store,
                    onFinished = { showStartup = false }
                )
            } else {
                val valentineStore: ValentineCardStore = viewModel { ValentineCardStore() }
                ValentineCardScreen(store = valentineStore)
            }

            Button(
                onClick = { isDarkTheme = !isDarkTheme },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text(if (isDarkTheme) "Light" else "Dark")
            }
        }
    }
}
