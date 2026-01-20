package com.example.template

interface AnalyticsService {
    fun logEvent(eventName: String, params: Map<String, String>)
}

class MockAnalyticsService : AnalyticsService {
    override fun logEvent(eventName: String, params: Map<String, String>) {
        println("Analytics event logged: $eventName, params: $params")
    }
}
