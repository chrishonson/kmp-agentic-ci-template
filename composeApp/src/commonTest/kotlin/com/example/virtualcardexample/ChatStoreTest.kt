package com.example.virtualcardexample

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChatStoreTest {
    private val mockChatService = object : ChatService {
        override fun getMessages(): Flow<List<ChatMessage>> = flowOf(emptyList())
        override fun isBotTyping(): Flow<Boolean> = flowOf(false)
        override suspend fun sendMessage(sender: String, content: String) {}
    }

    private val mockAnalytics = object : AnalyticsService {
        var loggedEvents = mutableListOf<String>()
        override fun logEvent(eventName: String, params: Map<String, String>) {
            loggedEvents.add(eventName)
        }
    }

    @Test
    fun testInitialState() = runTest {
        val store = ChatStore(mockChatService, mockAnalytics, CoroutineScope(Dispatchers.Unconfined))
        assertEquals(emptyList(), store.state.value.messages)
        assertEquals(false, store.state.value.isBotTyping)
        assertTrue(mockAnalytics.loggedEvents.contains("chat_session_started"))
    }
}
