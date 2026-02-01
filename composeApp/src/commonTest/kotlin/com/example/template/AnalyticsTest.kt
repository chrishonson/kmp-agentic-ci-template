package com.example.template

import kotlin.test.Test
import kotlin.test.assertEquals

class AnalyticsTest {

    private class FakeAnalyticsService : AnalyticsService {
        var lastEventName: String? = null
        var lastParams: Map<String, String>? = null

        override fun logEvent(eventName: String, params: Map<String, String>) {
            lastEventName = eventName
            lastParams = params
        }
    }

    @Test
    fun testScreenViewEvent() {
        val service = FakeAnalyticsService()
        val analytics = Analytics(service)
        analytics.logEvent(AnalyticsEvent.ScreenView("Home"))
        assertEquals("screen_view", service.lastEventName)
        assertEquals("Home", service.lastParams?.get("screen_name"))
    }

    @Test
    fun testButtonClickEvent() {
        val service = FakeAnalyticsService()
        val analytics = Analytics(service)
        analytics.logEvent(AnalyticsEvent.ButtonClick("submit_btn"))
        assertEquals("button_click", service.lastEventName)
        assertEquals("submit_btn", service.lastParams?.get("button_id"))
    }
}
