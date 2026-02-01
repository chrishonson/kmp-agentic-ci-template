package com.example.template

interface EventLoggerService {
    fun logEvent(eventName: String, params: Map<String, String>? = null)
}

class ConsoleEventLoggerService : EventLoggerService {
    override fun logEvent(eventName: String, params: Map<String, String>?) {
        println("Analytics Event: $eventName, Params: $params")
    }
}

class CompositeEventLoggerService(private val loggers: List<EventLoggerService>) : EventLoggerService {
    override fun logEvent(eventName: String, params: Map<String, String>?) {
        loggers.forEach { it.logEvent(eventName, params) }
    }
}
