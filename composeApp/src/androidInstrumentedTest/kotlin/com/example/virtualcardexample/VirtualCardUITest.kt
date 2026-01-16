package com.example.virtualcardexample

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class VirtualCardUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun loginAs(username: String) {
        // Wait for splash to finish (2 seconds in App.kt)
        composeTestRule.mainClock.advanceTimeBy(3000)

        composeTestRule.onNodeWithTag("UsernameField").performTextInput(username)
        composeTestRule.onNodeWithTag("PasswordField").performTextInput("password123")
        composeTestRule.onNodeWithTag("LoginButton").performClick()
    }

    @Test
    fun testLoginAndGreetingDisplayed() {
        composeTestRule.setContent {
            App()
        }

        val testUser = "JohnDoe"
        loginAs(testUser)

        composeTestRule.onNodeWithText("$testUser, will you be my valentine?").assertIsDisplayed()
    }

    @Test
    fun testCardDetailsAreDisplayedAfterLogin() {
        composeTestRule.setContent {
            App()
        }

        loginAs("JohnDoe")

        composeTestRule.onNodeWithTag("CreditCard").assertIsDisplayed()
        composeTestRule.onNodeWithTag("CardNumber").assertIsDisplayed()
    }

    @Test
    fun testToggleLockAfterLogin() {
        composeTestRule.setContent {
            App()
        }

        loginAs("JohnDoe")

        // Initial state
        composeTestRule.onNodeWithTag("CreditCard").performClick()

        // Should show locked state
        composeTestRule.onNodeWithText("CARD LOCKED").assertIsDisplayed()

        // Toggle back
        composeTestRule.onNodeWithTag("CreditCard").performClick()
        composeTestRule.onNodeWithTag("CardNumber").assertIsDisplayed()
    }
}
