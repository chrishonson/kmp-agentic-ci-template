package com.example.virtualcardexample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val MASKED_CARD_SUFFIX_LENGTH = 4

class VirtualCardStore(
    private val cardDetailsService: CardDetailsService = CardDetailsService(),
    private val analyticsService: AnalyticsService = MockAnalyticsService()
) : ViewModel() {

    private val analyticsMiddleware: AnalyticsMiddleware = AnalyticsMiddleware(analyticsService)

    private val _state = MutableStateFlow(VirtualCardState())
    val state: StateFlow<VirtualCardState> = _state.asStateFlow()

    private var _cachedCardDetails: CardDetails? = null

    init {
        dispatch(VirtualCardIntent.LoadCardDetails)
    }

    fun dispatch(intent: VirtualCardIntent) {
        analyticsMiddleware.logEvent(intent, _state.value)
        when (intent) {
            VirtualCardIntent.ToggleVisibility -> {
                toggleVisibility()
            }
            VirtualCardIntent.LoadCardDetails -> loadCardDetails()
            VirtualCardIntent.ToggleLock -> {
                toggleLock()
            }
            VirtualCardIntent.ReplaceCard -> {
                replaceCard()
            }
        }
    }

    private fun toggleVisibility() {
        _state.update { currentState ->
            if (currentState.isLocked) {
                // If card is locked, cannot reveal details
                currentState.copy(isRevealed = false, buttonText = "Reveal Details")
            } else {
                val newRevealed = !currentState.isRevealed
                _cachedCardDetails?.let { details ->
                    currentState.copy(
                        isRevealed = newRevealed,
                        cardNumber = if (newRevealed) {
                            details.cardNumber
                        } else {
                            "**** **** **** ${details.cardNumber.takeLast(MASKED_CARD_SUFFIX_LENGTH)}"
                        },
                        expiry = if (newRevealed) details.expiry else "**/**",
                        cvv = if (newRevealed) details.cvv else "***",
                        buttonText = if (newRevealed) "Hide Details" else "Reveal Details"
                    )
                } ?: currentState // If details are not loaded yet, do nothing or handle as error
            }
        }
    }

    private fun toggleLock() {
        viewModelScope.launch {
            val currentLocked = _state.value.isLocked
            val message = if (currentLocked) "UNLOCKING CARD" else "LOCKING CARD"
            _state.update { it.copy(isLoading = true, loadingMessage = message) }
            val success = if (currentLocked) cardDetailsService.unlockCard() else cardDetailsService.lockCard()
            if (success) {
                _state.update { currentState ->
                    val newLocked = !currentState.isLocked
                    currentState.copy(
                        isLocked = newLocked,
                        // Hide details if locking
                        isRevealed = if (newLocked) false else currentState.isRevealed,
                        // Reset button if locking
                        buttonText = if (newLocked) "Reveal Details" else currentState.buttonText,
                        isLoading = false,
                        loadingMessage = null
                    )
                }
            } else {
                _state.update { it.copy(isLoading = false, loadingMessage = null) }
            }
        }
    }

    private fun loadCardDetails() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    isRevealed = false,
                    buttonText = "Reveal Details",
                    isLocked = false,
                    loadingMessage = "LOADING CARD DETAILS"
                )
            }
            val cardDetails = cardDetailsService.fetchCardDetails()
            _cachedCardDetails = cardDetails
            _state.update { currentState ->
                currentState.copy(
                    cardNumber = "**** **** **** ${cardDetails.cardNumber.takeLast(MASKED_CARD_SUFFIX_LENGTH)}",
                    cardHolder = cardDetails.cardHolder,
                    expiry = "**/**", // Mask expiry initially
                    cvv = "***", // Mask CVV initially
                    isLoading = false,
                    isRevealed = false,
                    buttonText = "Reveal Details",
                    isLocked = false,
                    loadingMessage = null
                )
            }
        }
    }

    private fun replaceCard() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    isRevealed = false,
                    buttonText = "Reveal Details",
                    isLocked = false,
                    loadingMessage = "LOADING NEW CARD"
                )
            }
            val newCardDetails = cardDetailsService.replaceCard()
            _cachedCardDetails = newCardDetails
            _state.update { currentState ->
                currentState.copy(
                    cardNumber = "**** **** **** ${newCardDetails.cardNumber.takeLast(MASKED_CARD_SUFFIX_LENGTH)}",
                    cardHolder = newCardDetails.cardHolder,
                    expiry = "**/**",
                    cvv = "***",
                    isLoading = false,
                    isRevealed = false,
                    buttonText = "Reveal Details",
                    isLocked = false,
                    loadingMessage = null
                )
            }
        }
    }
}
