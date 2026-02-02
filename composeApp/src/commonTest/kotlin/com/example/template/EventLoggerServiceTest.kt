package com.example.template

import kotlin.test.Test
import kotlin.test.assertEquals

class EventLoggerServiceTest {
    private class FakeEventLogger : EventLoggerService {
        var lastEventName: String? = null
        var lastParams: Map<String, String>? = null

        override fun logEvent(eventName: String, params: Map<String, String>?) {
            lastEventName = eventName
            lastParams = params
        }
    }

    @Test
    fun testLogEvent() {
        val logger = FakeEventLogger()
        val params = mapOf("key" to "value")
        logger.logEvent("test_event", params)
        assertEquals("test_event", logger.lastEventName)
        assertEquals(params, logger.lastParams)
    }

    @Test
    fun testCompositeEventLogger() {
        val logger1 = FakeEventLogger()
        val logger2 = FakeEventLogger()
        val composite = CompositeEventLoggerService(listOf(logger1, logger2))
        val params = mapOf("foo" to "bar")
        composite.logEvent("composite_event", params)
        assertEquals("composite_event", logger1.lastEventName)
        assertEquals(params, logger1.lastParams)
        assertEquals("composite_event", logger2.lastEventName)
        assertEquals(params, logger2.lastParams)
    }
}
