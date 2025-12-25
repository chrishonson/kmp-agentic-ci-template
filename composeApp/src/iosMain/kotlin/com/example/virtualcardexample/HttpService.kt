package com.example.virtualcardexample

import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSString
import platform.Foundation.create
import platform.Foundation.dataTaskWithURL
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class IosHttpService : HttpService {
    override suspend fun get(url: String): String = suspendCancellableCoroutine { continuation ->
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl == null) {
            continuation.resume("Error: Invalid URL")
            return@suspendCancellableCoroutine
        }
        val task = NSURLSession.sharedSession.dataTaskWithURL(nsUrl) { data, _, error ->
            if (error != null) {
                continuation.resume("Error: ${error.localizedDescription}")
                return@dataTaskWithURL
            }
            if (data == null) {
                continuation.resume("Error: No data received")
                return@dataTaskWithURL
            }

            val string = NSString.create(data = data, encoding = NSUTF8StringEncoding) as String?
            continuation.resume(string ?: "Error: Could not decode data")
        }
        task.resume()

        continuation.invokeOnCancellation {
            task.cancel()
        }
    }
}

actual fun getHttpService(): HttpService = IosHttpService()
