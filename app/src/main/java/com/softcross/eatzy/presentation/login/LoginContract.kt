package com.softcross.eatzy.presentation.login

object LoginContract {
    data class UiState(
        val isLoading: Boolean = false,
        val email: String = "",
        val password: String = "",
    )

    sealed class UiAction {
        data object SignInClick : UiAction()
        data class EmailChanged(val email: String) : UiAction()
        data class PasswordChanged(val password: String) : UiAction()
    }

    sealed class UiEffect {
        data class ShowSnackbar(val message: String) : UiEffect()
        data object NavigateToMainScreen : UiEffect()
    }
}