package com.example.template

import kotlin.test.Test
import kotlin.test.assertEquals

class GreetingDetailsTest {
    @Test
    fun `format returns correctly formatted string`() {
        val details = GreetingDetails("Kotlin", "Hello", "Welcome to KMP")
        val expected = "Hello, Kotlin! Welcome to KMP"
        assertEquals(expected, details.format())
    }
}
