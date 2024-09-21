package com.softcross.eatzy.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.domain.repository.FirebaseRepository
import com.softcross.eatzy.domain.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.softcross.eatzy.presentation.main.MainContract.UiAction
import com.softcross.eatzy.presentation.main.MainContract.UiEffect
import com.softcross.eatzy.presentation.main.MainContract.UiState
import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.data.dto.FavoriteFoodDto
import com.softcross.eatzy.domain.model.CartItem
import com.softcross.eatzy.domain.model.Food
import com.softcross.eatzy.domain.model.Location
import com.softcross.eatzy.domain.repository.CartRepository
import com.softcross.eatzy.presentation.producDetail.ProductDetailContract
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val firebaseRepository: FirebaseRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        getCartPrice()
        getLocations()
        getAllFoods()
    }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.SearchChanged -> {
                updateUiState { copy(searchKey = uiAction.key) }
                searchFoods()
            }
            is UiAction.OnQuitClick -> onQuitClick()
            is UiAction.OnFavoriteClick -> setFavoriteFood(uiAction.foodID)
            is UiAction.OnRetryClick -> getAllFoods()
            is UiAction.OnLocationClick -> onLocationClick()
            is UiAction.OnLocationItemClick -> updateUiState { copy(currentLocation = uiAction.location) }
            is UiAction.OnFoodAddClick -> addOnCart(uiAction.food)
            is UiAction.OnSignOutClick -> signOut()
        }
    }

    private fun onQuitClick() = viewModelScope.launch {
        emitUiEffect(UiEffect.ShowQuitDialog)
    }

    private fun onLocationClick() = viewModelScope.launch {
        if (uiState.value.locationList.isEmpty()) {
            getLocations()
        } else {
            emitUiEffect(UiEffect.ShowLocationBottomSheet)
        }
    }

    private fun getCartPrice() = viewModelScope.launch {
        val cartPrice = cartRepository.getCartPrice()
        updateUiState { copy(cartPrice = cartPrice) }
    }

    private fun setFavoriteFood(foodID: Int) = viewModelScope.launch {
        val updatedFoodList = uiState.value.allFoodList.map { food ->
            if (food.id == foodID) food.copy(isFavorite = !food.isFavorite) else food
        }
        val updatedCartList = uiState.value.foodList.map { food ->
            if (food.id == foodID) food.copy(isFavorite = !food.isFavorite) else food
        }
        updateUiState { copy(foodList = updatedCartList, allFoodList = updatedFoodList) }
        foodRepository.setFavoriteFoods(
            FavoriteFoodDto(
                id = foodID,
                userName = EatzySingleton.currentUsername
            )
        )
    }

    private fun getAllFoods() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        when (val result = foodRepository.getAllFoods()) {
            is ResponseState.Success -> {
                updateUiState {
                    copy(
                        foodList = result.result,
                        allFoodList = result.result,
                        isLoading = false
                    )
                }
            }

            is ResponseState.Error -> {
                updateUiState { copy(isLoading = false, errorMessage = "Error on fetching foods") }
            }
        }
    }

    private fun addOnCart(food: Food) = viewModelScope.launch {
        updateUiState {
            copy(successMessage = "")
        }
        when (val result = cartRepository.addFoodToCart(
            CartItem(
                id = 0,
                name = food.name,
                image = food.image,
                price = food.price,
                count = 1
            )
        )) {
            is ResponseState.Success -> {
                EatzySingleton.cartAmount.intValue.plus(1)
                updateUiState {
                    copy(
                        successMessage = "Food successfully added to cart",
                        cartPrice = (uiState.value.cartPrice + food.price).toInt()
                    )
                }
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

    private fun getLocations() = viewModelScope.launch {
        when (val result = firebaseRepository.getLocations()) {
            is ResponseState.Success -> {
                updateUiState {
                    copy(
                        locationList = result.result,
                        currentLocation = if (result.result.isNotEmpty()) result.result.first() else null,
                    )
                }
            }

            is ResponseState.Error -> {
                updateUiState {
                    copy(
                        errorMessage = "Error on fetching locations"
                    )
                }
            }
        }
    }

    private fun signOut() = viewModelScope.launch {
        firebaseRepository.signOutUser()
        emitUiEffect(UiEffect.NavigateToIntroduceScreen)
    }

    private fun searchFoods() = viewModelScope.launch {
        val searchKey = uiState.value.searchKey
        if (searchKey.isNotEmpty()) {
            updateUiState {
                copy(foodList = uiState.value.allFoodList.filter {
                    it.name.contains(
                        searchKey,
                        ignoreCase = true
                    )
                })
            }
        } else {
            updateUiState { copy(foodList = uiState.value.allFoodList) }
        }
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }

}