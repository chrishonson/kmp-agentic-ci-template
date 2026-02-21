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
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MockPostService : PostService {
    var postToReturn: Post = Post(1, 1, "Mock Title", "Mock Body")
    var errorToThrow: Exception? = null

    override suspend fun fetchPost(id: Int): Post {
        errorToThrow?.let { throw it }
        return postToReturn
    }

    override suspend fun fetchPosts(ids: List<Int>): List<Post> {
        errorToThrow?.let { throw it }
        return ids.map { postToReturn.copy(id = it) }
    }
}

 @OptIn(ExperimentalCoroutinesApi::class)
class StartupStoreTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockPostService: MockPostService

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockPostService = MockPostService()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() = runTest {
        val store = StartupStore(mockPostService)
        val state = store.state.value
        assertFalse(state.isLoading)
        assertFalse(state.isCompleted)
        assertFalse(state.isFinished)
        assertEquals(null, state.post)
    }

    @Test
    fun testInitializeIntentWithNetworkCall() = runTest {
        val store = StartupStore(mockPostService)
        store.dispatch(StartupIntent.Initialize)

        assertTrue(store.state.value.isLoading)

        advanceUntilIdle()

        assertFalse(store.state.value.isLoading)
        assertTrue(store.state.value.isCompleted)
        assertNotNull(store.state.value.post)
        assertEquals("Mock Title", store.state.value.post?.title)
    }

    @Test
    fun testAnimationFinishedIntent() = runTest {
        val store = StartupStore(mockPostService)
        store.dispatch(StartupIntent.AnimationFinished)

        assertTrue(store.state.value.isFinished)
    }

    @Test
    fun testRetryIntent() = runTest {
        val store = StartupStore(mockPostService)
        store.dispatch(StartupIntent.Retry)

        assertTrue(store.state.value.isLoading)

        advanceUntilIdle()

        assertFalse(store.state.value.isLoading)
        assertTrue(store.state.value.isCompleted)
        assertNotNull(store.state.value.post)
    }
}
