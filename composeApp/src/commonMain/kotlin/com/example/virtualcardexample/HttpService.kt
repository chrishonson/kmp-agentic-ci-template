package com.example.virtualcardexample

interface HttpService {
    suspend fun get(url: String): String
}

expect fun getHttpService(): HttpService
