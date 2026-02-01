package com.example.template

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EventLoggerServiceTest {

    private class FakeEventLogger : EventLoggerService {
        var loggedEvents = mutableListOf<Pair<String, Map<String, String>?>>()
        override fun logEvent(eventName: String, params: Map<String, String>?) {
            loggedEvents.add(eventName to params)
        }
    }

    @Test
    fun testConsoleEventLoggerService() {
        val service = ConsoleEventLoggerService()
        // Mainly verifying it doesn't crash
        service.logEvent("test_event", mapOf("key" to "value"))
        assertTrue(true)
    }

    @Test
    fun testCompositeEventLoggerService() {
        val logger1 = FakeEventLogger()
        val logger2 = FakeEventLogger()
        val composite = CompositeEventLoggerService(listOf(logger1, logger2))
        val params = mapOf("param1" to "value1")
        composite.logEvent("composite_event", params)
        assertEquals(1, logger1.loggedEvents.size)
        assertEquals("composite_event", logger1.loggedEvents[0].first)
        assertEquals(params, logger1.loggedEvents[0].second)
        assertEquals(1, logger2.loggedEvents.size)
        assertEquals("composite_event", logger2.loggedEvents[0].first)
        assertEquals(params, logger2.loggedEvents[0].second)
    }
}
