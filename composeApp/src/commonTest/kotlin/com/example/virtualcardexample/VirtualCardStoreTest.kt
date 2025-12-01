package com.example.virtualcardexample

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class VirtualCardStoreTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() = runTest {
        val store = VirtualCardStore()
        val state = store.state.value
        
        assertEquals("**** **** **** 3456", state.cardNumber)
        assertEquals("**/**", state.expiry)
        assertEquals("***", state.cvv)
        assertEquals("Reveal Details", state.buttonText)
        assertEquals(false, state.isRevealed)
    }

    @Test
    fun testToggleVisibility() = runTest {
        val store = VirtualCardStore()
        
        // Initial check
        assertEquals(false, store.state.value.isRevealed)

        // Toggle ON
        store.dispatch(VirtualCardIntent.ToggleVisibility)
        
        val revealedState = store.state.value
        assertEquals(true, revealedState.isRevealed)
        assertEquals("1234 5678 9012 3456", revealedState.cardNumber)
        assertEquals("12/28", revealedState.expiry)
        assertEquals("123", revealedState.cvv)
        assertEquals("Hide Details", revealedState.buttonText)

        // Toggle OFF
        store.dispatch(VirtualCardIntent.ToggleVisibility)
        
        val hiddenState = store.state.value
        assertEquals(false, hiddenState.isRevealed)
        assertEquals("**** **** **** 3456", hiddenState.cardNumber)
        assertEquals("Reveal Details", hiddenState.buttonText)
    }
}
