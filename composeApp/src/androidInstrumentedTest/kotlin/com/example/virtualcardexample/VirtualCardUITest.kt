package com.example.virtualcardexample

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class VirtualCardUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testValentineCardDisplaysCorrectly() {
        // Verify the Valentine's Day card is displayed
        composeTestRule.onNodeWithTag("ValentineCard").assertIsDisplayed()

        // Verify the title
        composeTestRule.onNodeWithTag("ValentineTitle").assertIsDisplayed()
        composeTestRule.onNodeWithText("Happy Valentine's Day").assertIsDisplayed()

        // Verify the heart icon is displayed
        composeTestRule.onNodeWithTag("HeartIcon").assertIsDisplayed()

        // Verify the recipient name
        composeTestRule.onNodeWithTag("RecipientName").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dear My Love,").assertIsDisplayed()

        // Verify the message
        composeTestRule.onNodeWithTag("ValentineMessage").assertIsDisplayed()
        composeTestRule.onNodeWithText("You make my heart skip a beat!").assertIsDisplayed()

        // Verify the closing text
        composeTestRule.onNodeWithText("With Love").assertIsDisplayed()
    }
}
