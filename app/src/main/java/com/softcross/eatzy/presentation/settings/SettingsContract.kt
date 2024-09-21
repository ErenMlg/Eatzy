package com.softcross.eatzy.presentation.settings

import com.softcross.eatzy.domain.model.Location
import com.softcross.eatzy.domain.model.Payment

object SettingsContract {
    data class UiState(
        val isLoading: Boolean = false,
        val locations: List<Location> = emptyList(),
        val payments: List<Payment> = emptyList()
    )

    sealed class UiAction {
        data object OnClickLocations : UiAction()
        data object OnClickPayments : UiAction()
        data object OnQuitClick : UiAction()
        data object OnChangeLanguageClick : UiAction()
        data object OnQuitSubmitClick : UiAction()
    }

    sealed class UiEffect {
        data object ShowLocationBottomSheet : UiEffect()
        data object ShowPaymentBottomSheet : UiEffect()
        data object NavigateToIntroduceScreen : UiEffect()
        data object ShowLanguageDialog : UiEffect()
        data object ShowQuitDialog : UiEffect()
    }
}