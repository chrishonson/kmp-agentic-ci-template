package com.example.template

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
import kotlin.test.assertTrue
import kotlin.test.assertFalse

// -- Reducer Tests: Pure, no coroutines needed --

class ValentineCardReducerTest {

    @Test
    fun updateRecipientNameSetsName() {
        val state = ValentineCardState()
        val result = valentineCardReducer(state, ValentineCardAction.UpdateRecipientName("Alice"))
        assertEquals("Alice", result.recipientName)
    }

    @Test
    fun revealSetsIsRevealed() {
        val state = ValentineCardState()
        val result = valentineCardReducer(state, ValentineCardAction.Reveal)
        assertTrue(result.isRevealed)
    }

    @Test
    fun setMessageUpdatesMessage() {
        val state = ValentineCardState(message = "Old")
        val result = valentineCardReducer(state, ValentineCardAction.SetMessage("New"))
        assertEquals("New", result.message)
    }

    @Test
    fun updateRecipientNamePreservesOtherFields() {
        val state = ValentineCardState(
            isRevealed = true,
            message = "Hello",
            background = ValentineBackground.ROMANTIC_SUNSET
        )
        val result = valentineCardReducer(state, ValentineCardAction.UpdateRecipientName("Bob"))
        assertEquals("Bob", result.recipientName)
        assertTrue(result.isRevealed)
        assertEquals("Hello", result.message)
        assertEquals(ValentineBackground.ROMANTIC_SUNSET, result.background)
    }
}

// -- Action Creator Tests --

class ValentineCardActionCreatorTest {

    @Test
    fun nextMessageCyclesThroughMessages() {
        val messages = listOf("Msg 1", "Msg 2", "Msg 3")
        val creator = ValentineCardActionCreator(messages)
        val dispatched = mutableListOf<ValentineCardAction>()

        assertEquals("Msg 1", creator.currentMessage())

        creator.handleIntent(ValentineCardIntent.NextMessage) { dispatched.add(it) }
        assertEquals(ValentineCardAction.SetMessage("Msg 2"), dispatched.last())

        creator.handleIntent(ValentineCardIntent.NextMessage) { dispatched.add(it) }
        assertEquals(ValentineCardAction.SetMessage("Msg 3"), dispatched.last())

        creator.handleIntent(ValentineCardIntent.NextMessage) { dispatched.add(it) }
        assertEquals(ValentineCardAction.SetMessage("Msg 1"), dispatched.last())
    }

    @Test
    fun revealMessageDispatchesRevealAction() {
        val creator = ValentineCardActionCreator(listOf("Msg"))
        val dispatched = mutableListOf<ValentineCardAction>()

        creator.handleIntent(ValentineCardIntent.RevealMessage) { dispatched.add(it) }
        assertEquals(ValentineCardAction.Reveal, dispatched.single())
    }
}

// -- Integration Tests: Store + ActionCreator + Reducer --

@OptIn(ExperimentalCoroutinesApi::class)
class ValentineCardStoreTest {
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
        val store = ValentineCardStore()
        val state = store.state.value
        assertEquals("", state.recipientName)
        assertFalse(state.isRevealed)
        assertTrue(state.message.isNotEmpty())
        assertEquals(ValentineBackground.HEARTS_FLOATING, state.background)
    }

    @Test
    fun testCustomBackgroundInitialization() = runTest {
        val store = ValentineCardStore(background = ValentineBackground.ROMANTIC_SUNSET)
        assertEquals(ValentineBackground.ROMANTIC_SUNSET, store.state.value.background)
    }

    @Test
    fun testUpdateRecipientName() = runTest {
        val store = ValentineCardStore()
        store.dispatch(ValentineCardIntent.UpdateRecipientName("John"))
        assertEquals("John", store.state.value.recipientName)
    }

    @Test
    fun testRevealMessage() = runTest {
        val store = ValentineCardStore()
        assertFalse(store.state.value.isRevealed)
        store.dispatch(ValentineCardIntent.RevealMessage)
        assertTrue(store.state.value.isRevealed)
    }

    @Test
    fun testNextMessage() = runTest {
        val messages = listOf("Msg 1", "Msg 2")
        val store = ValentineCardStore(messages = messages)
        assertEquals("Msg 1", store.state.value.message)
        store.dispatch(ValentineCardIntent.NextMessage)
        assertEquals("Msg 2", store.state.value.message)
        store.dispatch(ValentineCardIntent.NextMessage)
        assertEquals("Msg 1", store.state.value.message)
    }

    @Test
    fun testEmptyMessagesFallback() = runTest {
        val store = ValentineCardStore(messages = emptyList())
        assertTrue(store.state.value.message.isNotEmpty())
    }

    @Test
    fun testSingleMessageList() = runTest {
        val messages = listOf("Only Message")
        val store = ValentineCardStore(messages = messages)
        assertEquals("Only Message", store.state.value.message)
        store.dispatch(ValentineCardIntent.NextMessage)
        assertEquals("Only Message", store.state.value.message)
    }

    @Test
    fun testUpdateRecipientNamePreservesOtherState() = runTest {
        val store = ValentineCardStore()
        store.dispatch(ValentineCardIntent.RevealMessage)
        val background = store.state.value.background
        val message = store.state.value.message
        store.dispatch(ValentineCardIntent.UpdateRecipientName("Alice"))
        val state = store.state.value
        assertEquals("Alice", state.recipientName)
        assertTrue(state.isRevealed)
        assertEquals(background, state.background)
        assertEquals(message, state.message)
    }
}
