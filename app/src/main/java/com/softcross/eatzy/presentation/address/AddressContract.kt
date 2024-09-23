package com.softcross.eatzy.presentation.address

import com.softcross.eatzy.domain.model.Location

object AddressContract {
    data class UiState(
        val isLoading: Boolean = false,
        val locationList : List<Location> = emptyList(),
        val selectedLocation : Location? = null,
        val errorMessage: String = "",
        val title: String = "",
        val country: String = "",
        val city: String = "",
        val district: String = "",
        val openAddress: String = "",
    )

    sealed class UiAction {
        data object OnSubmitClicked : UiAction()
        data class OnTitleChanged(val title: String) : UiAction()
        data class OnCountryChanged(val country: String) : UiAction()
        data class OnCityChanged(val city: String) : UiAction()
        data class OnDistrictChanged(val district: String) : UiAction()
        data class OnOpenAddressChanged(val openAddress: String) : UiAction()
        data object OnClearFields : UiAction()
    }

    sealed class UiEffect {
        data object OnNavigateProfile : UiEffect()
    }
}