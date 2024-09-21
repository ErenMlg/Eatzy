package com.softcross.eatzy.presentation.cart

import com.softcross.eatzy.domain.model.CartItem
import com.softcross.eatzy.domain.model.Payment

object CartContract {
    data class UiState(
        val isLoading: Boolean = true,
        val isCartLoading: Boolean = false,
        val promotionCount : Int = 0,
        val promotionCode: String = "",
        val payments: List<Payment> = emptyList(),
        val errorMessage: String = "",
        val cartFoods: List<CartItem> = emptyList(),
        val cartPrice: Int = 0
    )

    sealed class UiAction {
        data class RemoveFood(val cartItem: CartItem) : UiAction()
        data class AddFood(val cartItem: CartItem) : UiAction()
        data class OnPromotionCodeChanged(val promotionCode: String) : UiAction()
        data object ClearCart : UiAction()
        data object OnClearClick : UiAction()
        data object OnPromotionCodeSubmit : UiAction()
        data object OnClickPayments : UiAction()
        data object OnClickPaymentItem : UiAction()
    }

    sealed class UiEffect {
        data object ShowSureDeleteDialog : UiEffect()
        data object ShowPaymentsDialog : UiEffect()
    }
}