package com.example.exampleapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class AppUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testRevealButtonShowsDetails() {
        // Start the app - MainActivity calls App() automatically
        // composeTestRule.setContent { AppScreen() }
        // Not needed if MainActivity sets it, but we can override or just verify what's on screen.

        // If we want to test AppScreen specifically in isolation we could use createComposeRule,
        // but since we had issues, let's use the Activity rule.
        // MainActivity calls App(), which calls AppScreen().
        // So we don't need setContent unless we want to override.

        // Wait for the loading indicator to disappear, meaning details are loaded
        // Wait for the loading indicator to disappear, meaning details are loaded
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasContentDescription("Loading")).fetchSemanticsNodes().isEmpty()
        }
        composeTestRule.onNode(hasContentDescription("Loading")).assertDoesNotExist()

        // Check initial state (Hidden)
        composeTestRule.onNodeWithTag("RevealButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("RevealButton").assertTextEquals("Reveal Details")

        // Card Number should be masked, with the last 4 digits visible from the default mock data
        composeTestRule.onNodeWithTag("CardNumber").assertTextEquals("**** **** **** 3456")

        // Click Reveal
        composeTestRule.onNodeWithTag("RevealButton").performClick()

        // Check revealed state
        composeTestRule.onNodeWithTag("RevealButton").assertTextEquals("Hide Details")
        // The revealed card number should now be the full mock card number
        composeTestRule.onNodeWithTag("CardNumber").assertTextEquals("1234 5678 9012 3456")
        composeTestRule.onNodeWithText("12/28").assertIsDisplayed()
        composeTestRule.onNodeWithText("123").assertIsDisplayed()
    }
}
