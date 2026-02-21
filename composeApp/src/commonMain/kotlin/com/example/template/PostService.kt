package com.example.template

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json

interface PostService {
    suspend fun fetchPost(id: Int): Post
    suspend fun fetchPosts(ids: List<Int>): List<Post>
}

class KtorPostService(private val httpClient: HttpClient) : PostService {
    override suspend fun fetchPost(id: Int): Post {
        return httpClient.get("https://jsonplaceholder.typicode.com/posts/$id").body()
    }

    override suspend fun fetchPosts(ids: List<Int>): List<Post> = coroutineScope {
        ids.map {
            async { fetchPost(it) }
        }.awaitAll()
    }
}

private val httpClient by lazy {
    HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
}

private val postService by lazy { KtorPostService(httpClient) }

fun providePostService(): PostService = postService
