package com.example.template

data class GreetingDetails(
    val recipient: String,
    val sender: String,
    val message: String
) {
    fun format(): String {
        return "$sender, $recipient! $message"
    }
}
