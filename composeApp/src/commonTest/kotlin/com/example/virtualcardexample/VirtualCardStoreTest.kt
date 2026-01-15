package com.example.virtualcardexample

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
    fun `test initial state`() = runTest {
        val store = VirtualCardStore(this)
        val state = store.state.value
        assertFalse(state.isLocked)
        assertFalse(state.isLoading)
        assertEquals("**** **** **** 1234", state.cardNumber)
    }

    @Test
    fun `test toggle lock`() = runTest {
        val store = VirtualCardStore(this)
        store.dispatch(VirtualCardIntent.ToggleLock)
        assertTrue(store.state.value.isLocked)
        store.dispatch(VirtualCardIntent.ToggleLock)
        assertFalse(store.state.value.isLocked)
    }

    @Test
    fun `test refresh`() = runTest {
        val store = VirtualCardStore(this)
        store.dispatch(VirtualCardIntent.Refresh)
        assertTrue(store.state.value.isLoading)
        advanceUntilIdle()
        assertFalse(store.state.value.isLoading)
    }
}
