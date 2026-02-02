package com.example.template

import kotlin.test.Test
import kotlin.test.assertNotNull

class GreetingServiceTest {
    @Test
    fun testFetchGreeting() {
        val service = GreetingService()
        val greeting = service.fetchGreeting()
        assertNotNull(greeting.recipient)
        assertNotNull(greeting.sender)
        assertNotNull(greeting.message)
    }
}
