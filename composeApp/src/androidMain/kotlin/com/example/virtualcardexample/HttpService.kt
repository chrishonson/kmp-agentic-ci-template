package com.example.virtualcardexample

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

private const val TIMEOUT_MILLIS = 5000

class AndroidHttpService : HttpService {
    override suspend fun get(url: String): String = withContext(Dispatchers.IO) {
        val connection = URL(url).openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = TIMEOUT_MILLIS
            connection.readTimeout = TIMEOUT_MILLIS

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                "Error: $responseCode"
            }
        } catch (e: IOException) {
            "Exception: ${e.message}"
        } finally {
            connection.disconnect()
        }
    }
}

actual fun getHttpService(): HttpService = AndroidHttpService()
