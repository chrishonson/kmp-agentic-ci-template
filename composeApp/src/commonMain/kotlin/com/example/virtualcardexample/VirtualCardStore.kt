package com.example.virtualcardexample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VirtualCardStore(private val cardDetailsService: CardDetailsService = CardDetailsService()) : ViewModel() {

    private val _state = MutableStateFlow(VirtualCardState())
    val state: StateFlow<VirtualCardState> = _state.asStateFlow()

    private var _cachedCardDetails: CardDetails? = null

    init {
        dispatch(VirtualCardIntent.LoadCardDetails)
    }

    fun dispatch(intent: VirtualCardIntent) {
        when (intent) {
            VirtualCardIntent.ToggleVisibility -> toggleVisibility()
            VirtualCardIntent.LoadCardDetails -> loadCardDetails()
            VirtualCardIntent.ToggleLock -> toggleLock()
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
                        cardNumber = if (newRevealed) details.cardNumber else "**** **** **** ${details.cardNumber.takeLast(4)}",
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
            _state.update { it.copy(isLoading = true, loadingMessage = if (it.isLocked) "UNLOCKING CARD" else "LOCKING CARD") }
            val success = if (_state.value.isLocked) cardDetailsService.unlockCard() else cardDetailsService.lockCard()
            if (success) {
                _state.update { currentState ->
                    val newLocked = !currentState.isLocked
                    // Simplification: directly update state without _cachedCardDetails?.let for locking/unlocking.
                    // This will be handled in a separate step if necessary.
                    currentState.copy(
                        isLocked = newLocked,
                        isRevealed = if (newLocked) false else currentState.isRevealed, // Hide details if locking
                        buttonText = if (newLocked) "Reveal Details" else currentState.buttonText, // Reset button if locking
                        isLoading = false,
                        loadingMessage = null
                    )
                }
            } else {
                _state.update { it.copy(isLoading = false, loadingMessage = null) }
                // Handle error or show a message to the user if locking/unlocking failed
            }
        }
    }

    private fun loadCardDetails() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isRevealed = false, buttonText = "Reveal Details", isLocked = false, loadingMessage = "LOADING CARD DETAILS") }
            val cardDetails = cardDetailsService.fetchCardDetails()
            _cachedCardDetails = cardDetails
            _state.update { currentState ->
                currentState.copy(
                    cardNumber = "**** **** **** ${cardDetails.cardNumber.takeLast(4)}", // Mask card number initially
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
}