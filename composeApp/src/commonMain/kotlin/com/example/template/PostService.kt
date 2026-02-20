package com.example.template

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface PostService {
    suspend fun fetchPost(id: Int): Post
}

class KtorPostService(private val httpClient: HttpClient) : PostService {
    override suspend fun fetchPost(id: Int): Post {
        return httpClient.get("https://jsonplaceholder.typicode.com/posts/\$id").body()
    }
}
