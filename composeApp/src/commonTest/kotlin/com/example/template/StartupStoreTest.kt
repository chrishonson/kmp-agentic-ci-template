package com.example.template

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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class StartupStoreTest {
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() = runTest {
        val store = StartupStore()
        val state = store.state.value
        assertFalse(state.isLoading)
        assertFalse(state.isCompleted)
        assertFalse(state.isFinished)
    }

    @Test
    fun testInitializeIntent() = runTest {
        val store = StartupStore()
        store.dispatch(StartupIntent.Initialize)

        assertTrue(store.state.value.isLoading)

        advanceUntilIdle()

        assertFalse(store.state.value.isLoading)
        assertTrue(store.state.value.isCompleted)
    }

    @Test
    fun testAnimationFinishedIntent() = runTest {
        val store = StartupStore()
        store.dispatch(StartupIntent.AnimationFinished)

        assertTrue(store.state.value.isFinished)
    }

    @Test
    fun testRetryIntent() = runTest {
        val store = StartupStore()
        // Trigger initial failure simulation if we had one, but here we just test it runs init
        store.dispatch(StartupIntent.Retry)

        assertTrue(store.state.value.isLoading)

        advanceUntilIdle()

        assertFalse(store.state.value.isLoading)
        assertTrue(store.state.value.isCompleted)
    }
}
