package com.example.exampleapp

class MockAnalyticsService : AnalyticsService {
    override fun logEvent(eventName: String, params: Map<String, String>) {
        println("Analytics event logged: $eventName, params: $params")
    }
}
