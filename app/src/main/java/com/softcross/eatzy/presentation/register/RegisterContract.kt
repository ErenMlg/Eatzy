package com.softcross.eatzy.presentation.register

object RegisterContract {
    data class UiState(
        val isLoading: Boolean = false,
        val username:String = "",
        val email: String = "",
        val password: String = "",
        val fullName: String = "",
        val phoneNumber: String = "",
        val errorMessage:String = ""
    )

    sealed class UiAction {
        data object RegisterClick : UiAction()
        data class EmailChanged(val email: String) : UiAction()
        data class PasswordChanged(val password: String) : UiAction()
        data class FullNameChanged(val fullName: String) : UiAction()
        data class UserNameChanged(val userName: String) : UiAction()
        data class PhoneNumberChanged(val phoneNumber: String) : UiAction()
    }

    sealed class UiEffect {
        data class ShowSnackbar(val message: String) : UiEffect()
        data object NavigateToMainScreen : UiEffect()
    }
}