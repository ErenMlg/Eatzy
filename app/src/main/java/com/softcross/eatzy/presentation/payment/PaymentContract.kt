package com.softcross.eatzy.presentation.payment

import com.softcross.eatzy.domain.model.Payment

object PaymentContract {
    data class UiState(
        val isLoading: Boolean = false,
        val errorMessage: String = "",
        val selectedPayment: Payment? = null,
        val title: String = "",
        val cartNo: String = "",
        val cardHolder: String = "",
        val expiryDate: String = "",
        val cvv: String = "",
    )

    sealed class UiAction {
        data class OnTitleChanged(val title: String) : UiAction()
        data class OnCartNoChanged(val cartNo: String) : UiAction()
        data class OnCardHolderChanged(val cardHolder: String) : UiAction()
        data class OnExpiryDateChanged(val expiryDate: String) : UiAction()
        data class OnCvvChanged(val cvv: String) : UiAction()
        data object OnSubmitClicked : UiAction()
        data object OnClearFields : UiAction()
    }

    sealed class UiEffect {
        data object OnNavigateProfile : UiEffect()
    }
}