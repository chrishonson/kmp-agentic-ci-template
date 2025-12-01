package com.example.virtualcardexample

import kotlinx.coroutines.delay

// This class simulates a network service for fetching card details.
open class CardDetailsService {
    open suspend fun fetchCardDetails(): CardDetails {
        delay(2000) // Simulate network delay
        return CardDetails(
            cardNumber = "1234 5678 9012 3456",
            cardHolder = "Nick Antigravity",
            expiry = "12/28",
            cvv = "123"
        )
    }
}
