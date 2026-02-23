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
import kotlin.test.assertNull
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

// -- Reducer Tests: Pure, no coroutines needed --

class StartupReducerTest {

    @Test
    fun loadingStartedSetsIsLoading() {
        val state = StartupState()
        val result = startupReducer(state, StartupAction.LoadingStarted)
        assertTrue(result.isLoading)
        assertNull(result.error)
    }

    @Test
    fun loadingStartedClearsError() {
        val state = StartupState(error = "Previous error")
        val result = startupReducer(state, StartupAction.LoadingStarted)
        assertTrue(result.isLoading)
        assertNull(result.error)
    }

    @Test
    fun loadingSucceededSetsPost() {
        val post = Post(1, 1, "Title", "Body")
        val state = StartupState(isLoading = true)
        val result = startupReducer(state, StartupAction.LoadingSucceeded(post))
        assertFalse(result.isLoading)
        assertTrue(result.isCompleted)
        assertEquals(post, result.post)
    }

    @Test
    fun loadingFailedSetsError() {
        val state = StartupState(isLoading = true)
        val result = startupReducer(state, StartupAction.LoadingFailed("Network error"))
        assertFalse(result.isLoading)
        assertEquals("Network error", result.error)
    }

    @Test
    fun animationFinishedSetsIsFinished() {
        val state = StartupState()
        val result = startupReducer(state, StartupAction.AnimationFinished)
        assertTrue(result.isFinished)
    }
}

// -- Integration Tests: Store + ActionCreator + Reducer --

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
        val store = StartupStore(StartupActionCreator(mockPostService))
        val state = store.state.value
        assertFalse(state.isLoading)
        assertFalse(state.isCompleted)
        assertFalse(state.isFinished)
        assertEquals(null, state.post)
    }

    @Test
    fun testInitializeIntentWithNetworkCall() = runTest {
        val store = StartupStore(StartupActionCreator(mockPostService))
        store.dispatch(StartupIntent.Initialize)
        advanceUntilIdle()

        assertFalse(store.state.value.isLoading)
        assertTrue(store.state.value.isCompleted)
        assertNotNull(store.state.value.post)
        assertEquals("Mock Title", store.state.value.post?.title)
    }

    @Test
    fun testAnimationFinishedIntent() = runTest {
        val store = StartupStore(StartupActionCreator(mockPostService))
        store.dispatch(StartupIntent.AnimationFinished)

        advanceUntilIdle()

        assertTrue(store.state.value.isFinished)
    }

    @Test
    fun testRetryIntent() = runTest {
        val store = StartupStore(StartupActionCreator(mockPostService))
        store.dispatch(StartupIntent.Retry)
        advanceUntilIdle()

        assertFalse(store.state.value.isLoading)
        assertTrue(store.state.value.isCompleted)
        assertNotNull(store.state.value.post)
    }

    @Test
    fun testNetworkErrorSetsErrorState() = runTest {
        mockPostService.errorToThrow = RuntimeException("Connection failed")
        val store = StartupStore(StartupActionCreator(mockPostService))
        store.dispatch(StartupIntent.Initialize)

        advanceUntilIdle()

        assertFalse(store.state.value.isLoading)
        assertFalse(store.state.value.isCompleted)
        assertEquals("Connection failed", store.state.value.error)
    }
}
