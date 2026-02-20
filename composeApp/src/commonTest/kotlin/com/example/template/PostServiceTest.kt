package com.example.template

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class PostServiceTest {

    @Test
    fun `fetchPost returns a post when successful`() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"userId": 1, "id": 1, "title": "Test Title", "body": "Test Body"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val service = KtorPostService(httpClient)
        val post = service.fetchPost(1)

        assertEquals(1, post.id)
        assertEquals("Test Title", post.title)
    }
}
