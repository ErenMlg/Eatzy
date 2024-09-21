package com.softcross.eatzy.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.domain.repository.FirebaseRepository
import com.softcross.eatzy.presentation.main.MainContract.UiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsContract.UiState())
    val uiState: StateFlow<SettingsContract.UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<SettingsContract.UiEffect>() }
    val uiEffect: Flow<SettingsContract.UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    fun onAction(uiAction: SettingsContract.UiAction) {
        when (uiAction) {
            is SettingsContract.UiAction.OnClickLocations -> showLocationBottomSheet()
            is SettingsContract.UiAction.OnClickPayments -> showPaymentBottomSheet()
            is SettingsContract.UiAction.OnQuitClick -> showQuitDialog()
            is SettingsContract.UiAction.OnChangeLanguageClick -> changeLanguage()
            is SettingsContract.UiAction.OnQuitSubmitClick -> signOut()
        }
    }

    init {
        getLocations()
        getPayments()
    }

    private fun showQuitDialog() = viewModelScope.launch {
        emitUiEffect(SettingsContract.UiEffect.ShowQuitDialog)
    }

    private fun changeLanguage() = viewModelScope.launch {
        emitUiEffect(SettingsContract.UiEffect.ShowLanguageDialog)
    }

    private fun signOut() = viewModelScope.launch {
        firebaseRepository.signOutUser()
        emitUiEffect(SettingsContract.UiEffect.NavigateToIntroduceScreen)
    }

    private fun showLocationBottomSheet() = viewModelScope.launch {
        emitUiEffect(SettingsContract.UiEffect.ShowLocationBottomSheet)
    }

    private fun showPaymentBottomSheet() = viewModelScope.launch {
        emitUiEffect(SettingsContract.UiEffect.ShowPaymentBottomSheet)
    }

    private fun getPayments() = viewModelScope.launch {
        when (val result = firebaseRepository.getPayments()) {
            is ResponseState.Success -> {
                updateUiState { copy(payments = result.result) }
            }

            is ResponseState.Error -> {
            }
        }
    }


    private fun getLocations() = viewModelScope.launch {
        when (val result = firebaseRepository.getLocations()) {
            is ResponseState.Success -> {
                updateUiState { copy(locations = result.result) }
            }

            is ResponseState.Error -> {
                Log.i("SettingsViewModel", "getLocations: ${result.exception}")
            }
        }
    }


    private fun updateUiState(block: SettingsContract.UiState.() -> SettingsContract.UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: SettingsContract.UiEffect) {
        _uiEffect.send(uiEffect)
    }

}