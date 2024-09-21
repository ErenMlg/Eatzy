package com.softcross.eatzy.presentation.promotions

import com.softcross.eatzy.domain.model.Promotion

object PromotionContract {
    data class UiState(
        val isLoading: Boolean = true,
        val promotions: List<Promotion> = emptyList(),
        val errorMessage: String = "",
    )

    sealed class UiAction {
        data object OnRetryClicked : UiAction()
    }

}