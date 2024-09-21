package com.softcross.eatzy.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.eatzy.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.softcross.eatzy.presentation.register.RegisterContract.UiState
import com.softcross.eatzy.presentation.register.RegisterContract.UiAction
import com.softcross.eatzy.presentation.register.RegisterContract.UiEffect
import com.softcross.eatzy.common.ResponseState
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }



    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.EmailChanged -> updateUiState { copy(email = uiAction.email) }
            is UiAction.FullNameChanged -> updateUiState { copy(fullName = uiAction.fullName) }
            is UiAction.UserNameChanged -> updateUiState { copy(username = uiAction.userName) }
            is UiAction.PasswordChanged -> updateUiState { copy(password = uiAction.password) }
            is UiAction.PhoneNumberChanged -> updateUiState { copy(phoneNumber = uiAction.phoneNumber) }
            is UiAction.RegisterClick -> {
                updateUiState { copy(isLoading = true, ) }
                register()
            }
        }
    }

    private fun register() = viewModelScope.launch {
        when (val result = firebaseRepository.registerUser(
            uiState.value.email,
            uiState.value.password,
            uiState.value.fullName,
            uiState.value.username,
            uiState.value.phoneNumber
        )) {
            is ResponseState.Error -> emitUiEffect(UiEffect.ShowSnackbar(result.exception.message.toString()))
            is ResponseState.Success -> emitUiEffect(UiEffect.NavigateToMainScreen)
        }
        updateUiState { copy(isLoading = false) }
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }

}