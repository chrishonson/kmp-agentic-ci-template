package com.example.template

sealed class AnalyticsEvent {
    data class ScreenView(val screenName: String) : AnalyticsEvent()
}

class Analytics(private val service: AnalyticsService) {
    fun logEvent(event: AnalyticsEvent) {
        if (event is AnalyticsEvent.ScreenView) {
            service.logEvent("screen_view", mapOf("screen_name" to event.screenName))
        }
    }
}
