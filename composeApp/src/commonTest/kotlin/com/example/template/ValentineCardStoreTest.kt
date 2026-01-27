package com.example.template

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValentineCardStoreTest {

    @Test
    fun `initial state uses first message and defaults`() {
        val messages = listOf("Hello", "Love")
        val store = ValentineCardStore(messages)
        val state = store.state.value

        assertEquals("", state.recipientName)
        assertEquals(messages.first(), state.message)
        assertFalse(state.isRevealed)
        assertEquals(ValentineBackground.HEARTS_FLOATING, state.background)
    }

    @Test
    fun `update recipient name updates state`() {
        val store = ValentineCardStore(listOf("Hello"))

        store.dispatch(ValentineCardIntent.UpdateRecipientName("Taylor"))

        assertEquals("Taylor", store.state.value.recipientName)
    }

    @Test
    fun `reveal message marks state as revealed`() {
        val store = ValentineCardStore(listOf("Hello"))

        store.dispatch(ValentineCardIntent.RevealMessage)

        assertTrue(store.state.value.isRevealed)
    }

    @Test
    fun `next message cycles through messages`() {
        val messages = listOf("Hello", "Love")
        val store = ValentineCardStore(messages)

        store.dispatch(ValentineCardIntent.NextMessage)
        assertEquals("Love", store.state.value.message)

        store.dispatch(ValentineCardIntent.NextMessage)
        assertEquals("Hello", store.state.value.message)
    }
}
