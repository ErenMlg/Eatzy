package com.softcross.eatzy.presentation.producDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.data.dto.FavoriteFoodDto
import com.softcross.eatzy.domain.model.CartItem
import com.softcross.eatzy.domain.model.Food
import com.softcross.eatzy.domain.repository.CartRepository
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
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailContract.UiState())
    val uiState: StateFlow<ProductDetailContract.UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<ProductDetailContract.UiEffect>() }
    val uiEffect: Flow<ProductDetailContract.UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        val food = savedStateHandle.get<String>("food")?.let { Json.decodeFromString<Food>(it) }
        updateUiState { copy(food = food) }
    }

    fun onAction(uiAction: ProductDetailContract.UiAction) {
        when (uiAction) {
            is ProductDetailContract.UiAction.AddToCart -> addOnCart(uiState.value.food ?: Food())


            is ProductDetailContract.UiAction.IncreaseAmount -> updateUiState { copy(amount = amount + 1) }
            is ProductDetailContract.UiAction.DecreaseAmount -> {
                if (uiState.value.amount - 1 > 0) {
                    updateUiState { copy(amount = amount - 1) }
                }
            }

            is ProductDetailContract.UiAction.OnFavoriteClick -> setFavoriteFood(uiAction.foodID)
        }
    }

    private fun addOnCart(food: Food) = viewModelScope.launch {
        when (val result = cartRepository.addFoodToCart(
            CartItem(
                id = 0,
                name = food.name,
                image = food.image,
                price = food.price,
                count = uiState.value.amount
            )
        )) {
            is ResponseState.Success -> {
                emitUiEffect(ProductDetailContract.UiEffect.NavigateToCartScreen)
            }

            is ResponseState.Error -> {
                updateUiState {
                    copy(
                        errorMessage = result.exception.message
                            ?: "An error occurred on adding cart"
                    )
                }
            }
        }
    }

    private fun setFavoriteFood(foodID: Int) = viewModelScope.launch {
        foodRepository.setFavoriteFoods(
            FavoriteFoodDto(
                id = foodID,
                userName = EatzySingleton.currentUsername
            )
        )
        updateUiState { copy(food = food?.copy(isFavorite = !food.isFavorite)) }
    }

    private fun updateUiState(block: ProductDetailContract.UiState.() -> ProductDetailContract.UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: ProductDetailContract.UiEffect) {
        _uiEffect.send(uiEffect)
    }

}