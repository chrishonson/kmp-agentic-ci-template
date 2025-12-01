package com.example.virtualcardexample

class MockAnalyticsService : AnalyticsService {
    override fun logEvent(eventName: String, params: Map<String, String>) {
        println("Analytics event logged: $eventName, params: $params")
    }
}