package com.example.template

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class ValentineCardStoreTest {
    @Test
    fun testRevealMessage() = runTest {
        val store = ValentineCardStore()
        store.dispatch(ValentineCardIntent.RevealMessage)
        assertTrue(store.state.value.message.isNotEmpty())
    }
}
