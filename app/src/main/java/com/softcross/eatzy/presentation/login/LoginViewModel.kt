package com.softcross.eatzy.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.eatzy.common.EatzySingleton
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
import com.softcross.eatzy.presentation.login.LoginContract.UiState
import com.softcross.eatzy.presentation.login.LoginContract.UiAction
import com.softcross.eatzy.presentation.login.LoginContract.UiEffect
import com.softcross.eatzy.common.ResponseState
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.SignInClick -> {
                updateUiState { copy(isLoading = true) }
                login()
            }
            is UiAction.EmailChanged -> updateUiState { copy(email = uiAction.email) }
            is UiAction.PasswordChanged -> updateUiState { copy(password = uiAction.password) }
        }
    }

    private fun login() = viewModelScope.launch{
        when(val result = firebaseRepository.loginUser(uiState.value.email.trim(), uiState.value.password.trim())){
            is ResponseState.Error -> emitUiEffect(UiEffect.ShowSnackbar(result.exception.message.toString()))
            is ResponseState.Success -> {
                EatzySingleton.currentUser = result.result
                EatzySingleton.currentUsername = result.result.username
                emitUiEffect(UiEffect.NavigateToMainScreen)
            }
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