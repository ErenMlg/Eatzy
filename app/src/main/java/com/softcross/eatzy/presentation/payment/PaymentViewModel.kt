package com.softcross.eatzy.presentation.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.domain.model.Location
import com.softcross.eatzy.domain.model.Payment
import com.softcross.eatzy.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentContract.UiState())
    val uiState: StateFlow<PaymentContract.UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<PaymentContract.UiEffect>() }
    val uiEffect: Flow<PaymentContract.UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        val selectedPayment =
            savedStateHandle.get<String>("data")?.let { Json.decodeFromString<Payment>(it) }
        updateUiState {
            copy(
                selectedPayment = selectedPayment,
                title = selectedPayment?.title ?: "",
                cartNo = selectedPayment?.cartNumber ?: "",
                cardHolder = selectedPayment?.cartHolderName ?: "",
                expiryDate = selectedPayment?.cartExpiryDate ?: "",
                cvv = selectedPayment?.cartCVC ?: ""
            )
        }
    }

    fun onAction(uiAction: PaymentContract.UiAction) {
        when (uiAction) {
            is PaymentContract.UiAction.OnSubmitClicked -> {
                addNewPayment()
            }

            is PaymentContract.UiAction.OnTitleChanged -> updateUiState {
                copy(title = uiAction.title)
            }

            is PaymentContract.UiAction.OnCartNoChanged -> updateUiState {
                copy(cartNo = uiAction.cartNo)
            }

            is PaymentContract.UiAction.OnCardHolderChanged -> updateUiState {
                copy(cardHolder = uiAction.cardHolder)
            }

            is PaymentContract.UiAction.OnExpiryDateChanged -> updateUiState {
                copy(expiryDate = uiAction.expiryDate)
            }

            is PaymentContract.UiAction.OnCvvChanged -> updateUiState {
                copy(cvv = uiAction.cvv)
            }

            is PaymentContract.UiAction.OnClearFields -> updateUiState {
                copy(
                    title = "",
                    cartNo = "",
                    cardHolder = "",
                    expiryDate = "",
                    cvv = ""
                )
            }
        }
    }

    private fun addNewPayment() = viewModelScope.launch {
        if (uiState.value.selectedPayment == null) {
            when (val result = firebaseRepository.addPayment(
                Payment(
                    id = "",
                    title = uiState.value.title,
                    cartNumber = uiState.value.cartNo,
                    cartHolderName = uiState.value.cardHolder,
                    cartExpiryDate = uiState.value.expiryDate,
                    cartCVC = uiState.value.cvv
                )
            )) {
                is ResponseState.Success -> {
                    emitUiEffect(PaymentContract.UiEffect.OnNavigateProfile)
                }

                is ResponseState.Error -> {
                    updateUiState {
                        copy(errorMessage = result.exception.message ?: "An error occurred")
                    }
                }
            }
        } else {
            when (val result = firebaseRepository.updatePayment(
                uiState.value.selectedPayment!!.id,
                uiState.value.selectedPayment!!.copy(
                    title = uiState.value.title,
                    cartNumber = uiState.value.cartNo,
                    cartHolderName = uiState.value.cardHolder,
                    cartExpiryDate = uiState.value.expiryDate,
                    cartCVC = uiState.value.cvv
                )
            )) {
                is ResponseState.Success -> {
                    emitUiEffect(PaymentContract.UiEffect.OnNavigateProfile)
                }

                is ResponseState.Error -> {
                    updateUiState {
                        copy(errorMessage = result.exception.message ?: "An error occurred")
                    }
                }
            }
        }

    }

    private fun updateUiState(block: PaymentContract.UiState.() -> PaymentContract.UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: PaymentContract.UiEffect) {
        _uiEffect.send(uiEffect)
    }

}