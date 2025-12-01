package com.example.virtualcardexample

class AnalyticsMiddleware(private val analyticsService: AnalyticsService) {

    fun logEvent(intent: VirtualCardIntent, state: VirtualCardState) {
        when (intent) {
            VirtualCardIntent.LoadCardDetails -> {
                analyticsService.logEvent("virtual_card_screen_viewed")
            }
            VirtualCardIntent.ToggleVisibility -> {
                analyticsService.logEvent(
                    "virtual_card_details_visibility_toggled",
                    mapOf("is_revealed" to "${!state.isRevealed}")
                )
            }
            VirtualCardIntent.ToggleLock -> {
                analyticsService.logEvent(
                    "virtual_card_lock_toggled",
                    mapOf("is_locked" to "${!state.isLocked}")
                )
            }
            VirtualCardIntent.ReplaceCard -> {
                analyticsService.logEvent("virtual_card_replaced")
            }
        }
    }
}
