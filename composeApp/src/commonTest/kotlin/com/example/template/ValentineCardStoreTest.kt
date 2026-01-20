package com.example.template

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValentineCardStoreTest {

    @Test
    fun `initial state is hidden`() {
        val store = ValentineCardStore()
        assertFalse(store.state.value.isRevealed)
        assertEquals("", store.state.value.message)
        assertEquals("", store.state.value.recipientName)
    }

    @Test
    fun `updating recipient name updates state`() {
        val store = ValentineCardStore()
        store.dispatch(ValentineCardIntent.UpdateRecipientName("Alice"))
        assertEquals("Alice", store.state.value.recipientName)
    }

    @Test
    fun `revealing message updates state`() {
        val store = ValentineCardStore()
        store.dispatch(ValentineCardIntent.RevealMessage)
        assertTrue(store.state.value.isRevealed)
        assertTrue(store.state.value.message.isNotEmpty())
    }

    @Test
    fun `cycling next message updates message`() {
        val store = ValentineCardStore()
        store.dispatch(ValentineCardIntent.RevealMessage)
        val firstMessage = store.state.value.message
        
        store.dispatch(ValentineCardIntent.NextMessage)
        val secondMessage = store.state.value.message
        
        assertTrue(firstMessage != secondMessage)
    }
}
