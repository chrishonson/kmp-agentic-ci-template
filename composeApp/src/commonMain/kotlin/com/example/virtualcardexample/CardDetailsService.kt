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

    open suspend fun lockCard(): Boolean {
        delay(1000)
        return true
    }

    open suspend fun unlockCard(): Boolean {
        delay(1000)
        return true
    }

    open suspend fun replaceCard(): CardDetails {
        delay(2000)
        return CardDetails(
            cardNumber = "9876 5432 1098 7654",
            cardHolder = "Nick Antigravity",
            expiry = "01/30",
            cvv = "456"
        )
    }
}