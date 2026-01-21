package com.example.template

class GreetingService {
    private val greetings = listOf(
        GreetingDetails("My Love", "Your Forever", "You make every day feel special."),
        GreetingDetails("Sweetheart", "Always Yours", "I'm so lucky to have you in my life."),
        GreetingDetails("Darling", "With Love", "Thinking of you today and always.")
    )

    fun fetchGreeting(): GreetingDetails {
        return greetings.random()
    }
}
