package com.example.virtualcardexample

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.delay // Import delay
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class VirtualCardStoreTest {

    private val testDispatcher = StandardTestDispatcher()

    // Mock CardDetailsService for testing
    class MockCardDetailsService : CardDetailsService() {
        var lockCardCalled = false
        var unlockCardCalled = false
        var replaceCardCalled = false

        fun resetCalls() {
            lockCardCalled = false
            unlockCardCalled = false
            replaceCardCalled = false
        }

        override suspend fun fetchCardDetails(): CardDetails {
            delay(100) // Simulate a network delay
            return CardDetails(
                cardNumber = "1111 2222 3333 4444",
                cardHolder = "Test User",
                expiry = "11/22",
                cvv = "789"
            )
        }

        override suspend fun lockCard(): Boolean {
            lockCardCalled = true
            delay(100) // Simulate network delay for locking
            return true
        }

        override suspend fun unlockCard(): Boolean {
            unlockCardCalled = true
            delay(100) // Simulate network delay for unlocking
            return true
        }

        override suspend fun replaceCard(): CardDetails {
            replaceCardCalled = true
            delay(100)
            return CardDetails(
                cardNumber = "5555 6666 7777 8888",
                cardHolder = "Test User Replaced",
                expiry = "12/23",
                cvv = "012"
            )
        }
    }

    private lateinit var mockService: MockCardDetailsService

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockService = MockCardDetailsService()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() = runTest {
        val store = VirtualCardStore(mockService)
        testDispatcher.scheduler.runCurrent()

        assertTrue(store.state.value.isLoading)
        assertEquals("LOADING CARD DETAILS", store.state.value.loadingMessage)
        assertFalse(store.state.value.isRevealed)
        assertFalse(store.state.value.isLocked)

        advanceUntilIdle()
        val state = store.state.value

        assertEquals("**** **** **** 4444", state.cardNumber)
        assertEquals("Test User", state.cardHolder)
        assertEquals("**/**", state.expiry)
        assertEquals("***", state.cvv)
        assertEquals("Reveal Details", state.buttonText)
        assertFalse(state.isRevealed)
        assertFalse(state.isLoading)
        assertFalse(state.isLocked)
        assertEquals(null, state.loadingMessage)
    }

    @Test
    fun testToggleVisibility() = runTest {
        val store = VirtualCardStore(mockService)
        advanceUntilIdle() // Complete initial loading

        assertFalse(store.state.value.isRevealed)
        assertFalse(store.state.value.isLoading)
        assertFalse(store.state.value.isLocked)

        // Toggle ON
        store.dispatch(VirtualCardIntent.ToggleVisibility)

        val revealedState = store.state.value
        assertTrue(revealedState.isRevealed)
        assertEquals("1111 2222 3333 4444", revealedState.cardNumber)
        assertEquals("11/22", revealedState.expiry)
        assertEquals("789", revealedState.cvv)
        assertEquals("Hide Details", revealedState.buttonText)
        assertFalse(revealedState.isLoading)
        assertFalse(revealedState.isLocked)

        // Toggle OFF
        store.dispatch(VirtualCardIntent.ToggleVisibility)

        val hiddenState = store.state.value
        assertFalse(hiddenState.isRevealed)
        assertEquals("**** **** **** 4444", hiddenState.cardNumber)
        assertEquals("Reveal Details", hiddenState.buttonText)
        assertFalse(hiddenState.isLoading)
        assertFalse(hiddenState.isLocked)
    }

    @Test
    fun testLoadCardDetails() = runTest {
        val store = VirtualCardStore(mockService)
        advanceUntilIdle() // Complete initial loading

        // Simulate a revealed state before loading new details
        store.dispatch(VirtualCardIntent.ToggleVisibility)
        assertTrue(store.state.value.isRevealed)
        assertFalse(store.state.value.isLoading)
        assertFalse(store.state.value.isLocked)

        // Dispatch LoadCardDetails intent
        store.dispatch(VirtualCardIntent.LoadCardDetails)

        testDispatcher.scheduler.runCurrent() // Allow coroutine to start and update isLoading
        assertTrue(store.state.value.isLoading)
        assertFalse(store.state.value.isRevealed) // Should reset reveal state
        assertEquals("Reveal Details", store.state.value.buttonText)
        assertFalse(store.state.value.isLocked) // Should reset locked state
        assertEquals("LOADING CARD DETAILS", store.state.value.loadingMessage)

        advanceUntilIdle()

        assertFalse(store.state.value.isLoading)
        assertFalse(store.state.value.isRevealed)
        assertEquals("**** **** **** 4444", store.state.value.cardNumber)
        assertEquals("Test User", store.state.value.cardHolder)
        assertEquals("**/**", store.state.value.expiry)
        assertEquals("***", store.state.value.cvv)
        assertEquals("Reveal Details", store.state.value.buttonText)
        assertFalse(store.state.value.isLocked)
        assertEquals(null, store.state.value.loadingMessage)
    }

    @Test
    fun testLoadingStateDuringLoadCardDetails() = runTest {
        val store = VirtualCardStore(mockService)

        testDispatcher.scheduler.runCurrent()
        assertTrue(store.state.value.isLoading)
        assertFalse(store.state.value.isRevealed)
        assertEquals("Reveal Details", store.state.value.buttonText)
        assertFalse(store.state.value.isLocked)
        assertEquals("LOADING CARD DETAILS", store.state.value.loadingMessage)

        advanceUntilIdle() // complete the initial loading

        assertFalse(store.state.value.isLoading) // Should be false after initial load

        // Dispatch LoadCardDetails again
        store.dispatch(VirtualCardIntent.LoadCardDetails)

        testDispatcher.scheduler.runCurrent() // Allow coroutine to start and update isLoading
        assertTrue(store.state.value.isLoading)
        assertFalse(store.state.value.isRevealed) // Should also reset revealed state
        assertFalse(store.state.value.isLocked) // Should also reset locked state
        assertEquals("LOADING CARD DETAILS", store.state.value.loadingMessage)

        advanceUntilIdle()

        assertFalse(store.state.value.isLoading)
        assertEquals(null, store.state.value.loadingMessage)
    }

    @Test
    fun testToggleLock_lockCard_loadingState() = runTest {
        val store = VirtualCardStore(mockService)
        advanceUntilIdle() // Complete initial loading
        mockService.resetCalls() // Reset mock calls after initial load

        // Lock the card
        store.dispatch(VirtualCardIntent.ToggleLock)
        advanceUntilIdle() // Complete the lock operation including its delay

        val lockedState = store.state.value
        assertTrue(lockedState.isLocked)
        assertFalse(lockedState.isRevealed)
        assertFalse(lockedState.isLoading) // Should be false after completion
        assertEquals(null, lockedState.loadingMessage) // Should be null after completion
        assertTrue(mockService.lockCardCalled) // Should have been called
    }

    @Test
    fun testToggleLock_unlockCard_loadingState() = runTest {
        val store = VirtualCardStore(mockService)
        advanceUntilIdle() // Complete initial loading
        mockService.resetCalls() // Reset mock calls after initial load

        // Lock the card first, and wait for it to complete
        store.dispatch(VirtualCardIntent.ToggleLock)
        advanceUntilIdle()
        assertTrue(store.state.value.isLocked)
        mockService.resetCalls() // Reset mock calls after locking the card

        // Unlock the card
        store.dispatch(VirtualCardIntent.ToggleLock)
        advanceUntilIdle() // Complete the unlock operation including its delay

        val unlockedState = store.state.value
        assertFalse(unlockedState.isLocked)
        assertFalse(unlockedState.isRevealed) // Should remain hidden after unlock
        assertFalse(unlockedState.isLoading) // Should be false after completion
        assertEquals(null, unlockedState.loadingMessage) // Should be null after completion
        assertTrue(mockService.unlockCardCalled) // Should have been called
    }

    @Test
    fun testToggleLock_thenToggleVisibility() = runTest {
        val store = VirtualCardStore(mockService)
        advanceUntilIdle() // Complete initial loading

        // Lock the card
        store.dispatch(VirtualCardIntent.ToggleLock)
        advanceUntilIdle()
        assertTrue(store.state.value.isLocked)

        // Try to reveal details (should not work while locked)
        store.dispatch(VirtualCardIntent.ToggleVisibility)
        val stateAfterLockedVisibilityAttempt = store.state.value
        assertTrue(stateAfterLockedVisibilityAttempt.isLocked)
        assertFalse(stateAfterLockedVisibilityAttempt.isRevealed)
        assertEquals("Reveal Details", stateAfterLockedVisibilityAttempt.buttonText)

        // Unlock the card
        store.dispatch(VirtualCardIntent.ToggleLock)
        advanceUntilIdle()
        assertFalse(store.state.value.isLocked)

        // Now reveal details (should work)
        store.dispatch(VirtualCardIntent.ToggleVisibility)
        val stateAfterUnlockedVisibility = store.state.value
        assertFalse(stateAfterUnlockedVisibility.isLocked)
        assertTrue(stateAfterUnlockedVisibility.isRevealed)
        assertEquals("Hide Details", stateAfterUnlockedVisibility.buttonText)
        assertEquals("1111 2222 3333 4444", stateAfterUnlockedVisibility.cardNumber)
    }

    @Test
    fun testToggleLock_lockCard_finalState() = runTest {
        val store = VirtualCardStore(mockService)
        advanceUntilIdle() // Complete initial loading

        // Lock the card
        store.dispatch(VirtualCardIntent.ToggleLock)
        advanceUntilIdle() // Complete the lock operation

        val lockedState = store.state.value
        assertTrue(lockedState.isLocked)
        assertFalse(lockedState.isRevealed)
        assertEquals("**** **** **** 4444", lockedState.cardNumber)
        assertEquals("Reveal Details", lockedState.buttonText)
    }

    @Test
    fun testToggleLock_unlockCard_finalState() = runTest {
        val store = VirtualCardStore(mockService)
        advanceUntilIdle() // Complete initial loading

        // Lock the card first
        store.dispatch(VirtualCardIntent.ToggleLock)
        advanceUntilIdle()
        assertTrue(store.state.value.isLocked)

        // Unlock the card
        store.dispatch(VirtualCardIntent.ToggleLock)
        advanceUntilIdle() // Complete the unlock operation

        val unlockedState = store.state.value
        assertFalse(unlockedState.isLocked)
        assertFalse(unlockedState.isRevealed) // Should remain hidden after unlock
        assertEquals("Reveal Details", unlockedState.buttonText)
    }

    @Test
    fun testReplaceCard() = runTest {
        val store = VirtualCardStore(mockService)
        advanceUntilIdle() // Complete initial loading
        mockService.resetCalls()

        val initialCardNumberLast4 = store.state.value.cardNumber.takeLast(4)

        store.dispatch(VirtualCardIntent.ReplaceCard)
        testDispatcher.scheduler.runCurrent()
        assertTrue(store.state.value.isLoading)
        assertEquals("LOADING NEW CARD", store.state.value.loadingMessage)

        advanceUntilIdle()

        val newState = store.state.value
        assertFalse(newState.isLoading)
        assertEquals(null, newState.loadingMessage)
        assertEquals("**** **** **** 8888", newState.cardNumber)
        assertEquals("Test User Replaced", newState.cardHolder)
        assertEquals("**/**", newState.expiry)
        assertEquals("***", newState.cvv)
        assertEquals("Reveal Details", newState.buttonText)
        assertFalse(newState.isRevealed)
        assertFalse(newState.isLocked)
        assertTrue(mockService.replaceCardCalled)
        assertEquals("8888", newState.cardNumber.takeLast(4))
        assertTrue(initialCardNumberLast4 != "8888")
    }
}
