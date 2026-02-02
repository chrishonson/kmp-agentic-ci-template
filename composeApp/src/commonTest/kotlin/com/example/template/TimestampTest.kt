package com.example.template

import kotlin.test.Test
import kotlin.test.assertTrue

class TimestampTest {
    @Test
    fun testGetCurrentTimestamp() {
        val timestamp = getCurrentTimestamp()
        assertTrue(timestamp.isNotEmpty())
    }

    @Test
    fun testFormatTimestamp() {
        val formatted = formatTimestamp(123456789L)
        assertTrue(formatted.contains("123456789"))
    }
}
