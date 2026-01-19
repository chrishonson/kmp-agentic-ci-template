package com.example.exampleapp

interface EventLoggerService {
    fun logEvent(eventName: String, params: Map<String, String>? = null)
}

class ConsoleEventLoggerService : EventLoggerService {
    override fun logEvent(eventName: String, params: Map<String, String>?) {
        println("Analytics Event: $eventName, Params: $params")
    }
}
