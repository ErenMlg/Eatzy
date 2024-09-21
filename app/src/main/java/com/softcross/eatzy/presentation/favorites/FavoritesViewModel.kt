package com.softcross.eatzy.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.data.dto.FavoriteFoodDto
import com.softcross.eatzy.domain.model.Food
import com.softcross.eatzy.domain.repository.FoodRepository
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
class FavoritesViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesContract.UiState())
    val uiState: StateFlow<FavoritesContract.UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<FavoritesContract.UiEffect>() }
    val uiEffect: Flow<FavoritesContract.UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        getAllFavorites()
    }

    fun onAction(uiAction: FavoritesContract.UiAction) {
        when (uiAction) {
            is FavoritesContract.UiAction.OnClearFavorites -> clearFavorites()

            is FavoritesContract.UiAction.OnFavoriteClicked -> onFavoriteClick(uiAction.food)

            is FavoritesContract.UiAction.OnRetryClicked -> getAllFavorites()

            is FavoritesContract.UiAction.OnClearClicked -> clearFavoritesClicked()
        }
    }

    private fun clearFavoritesClicked() = viewModelScope.launch {
        if (uiState.value.favorites.isNotEmpty()){
            emitUiEffect(FavoritesContract.UiEffect.ShowClearFavoritesDialog)
        }
    }

    private fun clearFavorites() = viewModelScope.launch {
        foodRepository.clearAllFavorites()
        updateUiState { copy(favorites = emptyList()) }
    }

    private fun onFavoriteClick(food: Food) = viewModelScope.launch {
        foodRepository.setFavoriteFoods(
            FavoriteFoodDto(
                id = food.id,
                userName = EatzySingleton.currentUsername ?: ""
            )
        )
        updateUiState { copy(favorites = favorites.filterNot { it.id == food.id }) }
    }

    private fun getAllFavorites() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        when (val response = foodRepository.getAllFavorites()) {
            is ResponseState.Success -> {
                updateUiState {
                    copy(
                        isLoading = false,
                        favorites = response.result
                    )
                }
            }

            is ResponseState.Error -> {
                updateUiState {
                    copy(
                        isLoading = false,
                        errorMessage = response.exception.message ?: "An error occurred"
                    )
                }
            }
        }
    }

    private fun updateUiState(block: FavoritesContract.UiState.() -> FavoritesContract.UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: FavoritesContract.UiEffect) {
        _uiEffect.send(uiEffect)
    }

}