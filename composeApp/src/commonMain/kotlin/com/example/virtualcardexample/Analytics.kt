package com.example.virtualcardexample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

// A simple analytics service interface
interface AnalyticsService {
    fun logEvent(eventName: String, params: Map<String, String> = emptyMap())
}

// A concrete implementation of the analytics service that just prints to console
class ConsoleAnalyticsService : AnalyticsService {
    override fun logEvent(eventName: String, params: Map<String, String>) {
        println("[Analytics] Event: '$eventName', Params: $params")
    }
}

@Composable
fun rememberAnalyticsService(): AnalyticsService {
    return remember { ConsoleAnalyticsService() }
}
