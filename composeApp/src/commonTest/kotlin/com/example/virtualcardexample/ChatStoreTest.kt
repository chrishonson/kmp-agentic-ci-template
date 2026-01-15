package com.example.virtualcardexample

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ChatStoreTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var chatService: ChatService
    private lateinit var store: ChatStore

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        chatService = ChatServiceStub()
    }

    @Test
    fun testSendMessage() = runTest(testDispatcher) {
        store = ChatStore(chatService, this)
        val sender = "TestUser"
        val content = "Hello AWS"

        store.dispatch(ChatIntent.SendMessage(sender, content))

        val state = store.state.value
        // Should have the sent message and the bot response
        assertEquals(2, state.messages.size)
        assertEquals(sender, state.messages[0].sender)
        assertEquals(content, state.messages[0].content)
        assertEquals("AWS Bot", state.messages[1].sender)
        assertTrue(state.messages[1].content.contains("Hello"))
    }

    @Test
    fun testLoadMessages() = runTest(testDispatcher) {
        store = ChatStore(chatService, this)

        val state = store.state.value
        assertTrue(state.messages.isEmpty())

        chatService.sendMessage("User", "Initial")

        assertEquals(1, store.state.value.messages.size)
    }
}
