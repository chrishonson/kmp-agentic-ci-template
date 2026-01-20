package com.example.template

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValentineCardStoreTest {

    @Test
    fun `initial state is correct`() {
        val store = ValentineCardStore()
        val state = store.state.value
        assertEquals("My Love", state.recipientName)
        assertEquals("Happy Valentine's Day!", state.message)
        assertFalse(state.isOpened)
    }

    @Test
    fun `OpenCard intent updates isOpened to true`() {
        val store = ValentineCardStore()
        store.dispatch(ValentineCardIntent.OpenCard)
        assertTrue(store.state.value.isOpened)
    }

    @Test
    fun `UpdateRecipient intent updates recipientName`() {
        val store = ValentineCardStore()
        val newName = "Dearest"
        store.dispatch(ValentineCardIntent.UpdateRecipient(newName))
        assertEquals(newName, store.state.value.recipientName)
    }
}
