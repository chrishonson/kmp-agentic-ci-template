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
        }
    }

    private fun toggleVisibility() {
        _state.update { currentState ->
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

    private fun loadCardDetails() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isRevealed = false, buttonText = "Reveal Details") }
            val cardDetails = cardDetailsService.fetchCardDetails()
            _cachedCardDetails = cardDetails
            _state.update { currentState ->
                currentState.copy(
                    cardNumber = "**** **** **** ${cardDetails.cardNumber.takeLast(4)}", // Mask card number initially
                    cardHolder = cardDetails.cardHolder,
                    expiry = "**/**", // Mask expiry initially
                    cvv = "***", // Mask CVV initially
                    isLoading = false,
                )
            }
        }
    }
}
