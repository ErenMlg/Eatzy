package com.softcross.eatzy.presentation.promotions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.domain.repository.FirebaseRepository
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
class PromotionViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PromotionContract.UiState())
    val uiState: StateFlow<PromotionContract.UiState> get() = _uiState.asStateFlow()

    init {
        getPromotions()
    }

    fun onAction(uiAction: PromotionContract.UiAction) {
        when (uiAction) {
            is PromotionContract.UiAction.OnRetryClicked -> getPromotions()
        }
    }

    private fun getPromotions() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        when (val result = firebaseRepository.getPromotions()) {
            is ResponseState.Error -> {
                updateUiState {
                    copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "An error occurred"
                    )
                }
            }

            is ResponseState.Success -> {
                updateUiState { copy(isLoading = false, promotions = result.result) }
            }
        }
    }

    private fun updateUiState(block: PromotionContract.UiState.() -> PromotionContract.UiState) {
        _uiState.update(block)
    }


}