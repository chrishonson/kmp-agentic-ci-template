package com.example.exampleapp

class AnalyticsMiddleware(private val analyticsService: AnalyticsService) {

    fun logEvent(intent: AppIntent, state: AppState) {
        when (intent) {
            AppIntent.LoadCardDetails -> {
                analyticsService.logEvent("app_screen_viewed")
            }
            AppIntent.ToggleVisibility -> {
                analyticsService.logEvent(
                    "app_details_visibility_toggled",
                    mapOf("is_revealed" to "${!state.isRevealed}")
                )
            }
            AppIntent.ToggleLock -> {
                analyticsService.logEvent(
                    "app_lock_toggled",
                    mapOf("is_locked" to "${!state.isLocked}")
                )
            }
            AppIntent.ReplaceCard -> {
                analyticsService.logEvent("app_card_replaced")
            }
        }
    }
}
