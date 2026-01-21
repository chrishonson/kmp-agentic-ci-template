package com.example.template

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        var showStartup by remember { mutableStateOf(true) }

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
    }
}
