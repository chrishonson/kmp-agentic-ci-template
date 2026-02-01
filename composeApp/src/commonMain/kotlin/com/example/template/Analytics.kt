package com.example.template

sealed class AnalyticsEvent {
    data class ScreenView(val screenName: String) : AnalyticsEvent()
    data class ButtonClick(val buttonId: String) : AnalyticsEvent()
}

class Analytics(private val service: AnalyticsService) {
    fun logEvent(event: AnalyticsEvent) {
        when (event) {
            is AnalyticsEvent.ScreenView -> {
                service.logEvent("screen_view", mapOf("screen_name" to event.screenName))
            }
            is AnalyticsEvent.ButtonClick -> {
                service.logEvent("button_click", mapOf("button_id" to event.buttonId))
            }
        }
    }
}
