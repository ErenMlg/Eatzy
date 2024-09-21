package com.softcross.eatzy.presentation.producDetail

import com.softcross.eatzy.domain.model.Food

object ProductDetailContract {
    data class UiState(
        val isLoading: Boolean = false,
        val food: Food? = null,
        val amount: Int = 1,
        val errorMessage:String = ""
    )

    sealed class UiAction {
        object IncreaseAmount : UiAction()
        object DecreaseAmount : UiAction()
        object AddToCart : UiAction()
        data class OnFavoriteClick(val foodID: Int) : UiAction()
    }

    sealed class UiEffect {
        data object NavigateToCartScreen : UiEffect()
    }
}