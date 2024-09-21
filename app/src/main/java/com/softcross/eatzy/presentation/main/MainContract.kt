package com.softcross.eatzy.presentation.main

import com.softcross.eatzy.domain.model.Food
import com.softcross.eatzy.domain.model.Location

object MainContract {
    data class UiState(
        val isLoading: Boolean = true,
        val errorMessage: String = "",
        val successMessage: String = "",
        val locationList: List<Location> = emptyList(),
        val currentLocation: Location? = null,
        val allFoodList: List<Food> = emptyList(),
        val foodList: List<Food> = emptyList(),
        val searchKey: String = "",
        val cartPrice: Int = 0
    )

    sealed class UiAction {
        data class SearchChanged(val key: String) : UiAction()
        data class OnFavoriteClick(val foodID: Int) : UiAction()
        data object OnQuitClick : UiAction()
        data object OnRetryClick : UiAction()
        data object OnLocationClick : UiAction()
        data class OnLocationItemClick(val location: Location) : UiAction()
        data class OnFoodAddClick(val food: Food) : UiAction()
        data object OnSignOutClick : UiAction()
    }

    sealed class UiEffect {
        data object NavigateToIntroduceScreen : UiEffect()
        data object ShowLocationBottomSheet : UiEffect()
        data object ShowQuitDialog : UiEffect()
    }
}