package com.example.template

import kotlin.test.Test
import kotlin.test.assertEquals

class TimestampTest {
    @Test
    fun `formatTimestamp formats correctly`() {
        val timestamp = 123456789L
        val formatted = formatTimestamp(timestamp)
        assertEquals("Timestamp: 123456789", formatted)
    }

    @Test
    fun `formatTimestamp handles zero`() {
        assertEquals("Timestamp: 0", formatTimestamp(0L))
    }

    @Test
    fun `formatTimestamp handles negative`() {
        assertEquals("Timestamp: -100", formatTimestamp(-100L))
    }
}
