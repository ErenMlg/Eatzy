package com.softcross.eatzy.presentation.favorites

import com.softcross.eatzy.domain.model.Food

object FavoritesContract {
    data class UiState(
        val isLoading: Boolean = true,
        val favorites: List<Food> = emptyList(),
        val errorMessage: String = ""
    )

    sealed class UiAction {
        data object OnClearFavorites : UiAction()
        data class OnFavoriteClicked(val food: Food) : UiAction()
        data object OnRetryClicked : UiAction()
        data object OnClearClicked : UiAction()
    }

    sealed class UiEffect {
        data object ShowClearFavoritesDialog : UiEffect()
    }
}