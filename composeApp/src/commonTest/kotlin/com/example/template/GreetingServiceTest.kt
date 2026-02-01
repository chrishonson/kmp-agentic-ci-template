package com.example.template

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GreetingServiceTest {
    private val service = GreetingService()

    @Test
    fun `fetchGreeting returns a valid greeting`() {
        val greeting = service.fetchGreeting()
        assertNotNull(greeting.sender)
        assertNotNull(greeting.recipient)
        assertNotNull(greeting.message)
    }

    @Test
    fun `fetchGreeting returns random greetings`() {
        val results = mutableSetOf<GreetingDetails>()
        repeat(100) {
            results.add(service.fetchGreeting())
        }
        // With 45 greetings, the chance of getting only 1 unique after 100 tries is astronomically low
        assertTrue(results.size > 1, "Should return more than one unique greeting")
    }
}
