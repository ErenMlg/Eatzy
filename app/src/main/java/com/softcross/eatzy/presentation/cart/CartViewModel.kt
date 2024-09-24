package com.softcross.eatzy.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.eatzy.R
import com.softcross.eatzy.common.ContextProvider
import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.common.extension.mapResponse
import com.softcross.eatzy.domain.model.CartItem
import com.softcross.eatzy.domain.repository.CartRepository
import com.softcross.eatzy.domain.repository.FirebaseRepository
import com.softcross.eatzy.domain.repository.FoodRepository
import com.softcross.eatzy.presentation.settings.SettingsContract
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
class CartViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val cartRepository: CartRepository,
    private val provider: ContextProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartContract.UiState())
    val uiState: StateFlow<CartContract.UiState> get() = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<CartContract.UiEffect>() }
    val uiEffect: Flow<CartContract.UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        getCartFoods()
        getCartPrice()
    }

    fun onAction(uiAction: CartContract.UiAction) {
        when (uiAction) {
            is CartContract.UiAction.RemoveFood -> {
                if (uiAction.cartItem.count > 1) decreaseFood(uiAction.cartItem)
                else deleteFood(uiAction.cartItem)
            }

            is CartContract.UiAction.AddFood -> addFood(uiAction.cartItem)
            is CartContract.UiAction.ClearCart -> clearCart()
            is CartContract.UiAction.OnClearClick -> showSureDeleteDialog()
            is CartContract.UiAction.OnPromotionCodeChanged -> updateUiState {
                copy(promotionCode = uiAction.promotionCode)
            }

            is CartContract.UiAction.OnClickPayments -> getPayments()

            is CartContract.UiAction.OnPromotionCodeSubmit -> getPromotions()
            is CartContract.UiAction.OnClickPaymentItem -> {
                clearCart()
            }
        }
    }

    private fun getPayments() = viewModelScope.launch {
        when (val result = firebaseRepository.getPayments()) {
            is ResponseState.Success -> {
                updateUiState { copy(payments = result.result) }
                emitUiEffect(CartContract.UiEffect.ShowPaymentsDialog)
            }

            is ResponseState.Error -> {
            }
        }
    }

    private fun getPromotions() = viewModelScope.launch {
        updateUiState { copy(errorMessage = "") }
        if (uiState.value.promotionCount == 0) {
            if(uiState.value.cartPrice!=0){
                updateUiState { copy(isLoading = true) }
                when (val result =
                    firebaseRepository.findPromotionWithCode(uiState.value.promotionCode)) {
                    is ResponseState.Error -> {
                        updateUiState {
                            copy(
                                isLoading = false,
                                errorMessage = result.exception.message ?: provider.context.getString(
                                    R.string.an_unknown_error_occurred)
                            )
                        }
                    }

                    is ResponseState.Success -> {
                        val newCartPrice =
                            uiState.value.cartPrice - (uiState.value.cartPrice * result.result.discount / 100)
                        updateUiState {
                            copy(
                                isLoading = false,
                                cartPrice = newCartPrice,
                                promotionCount = 1
                            )
                        }
                    }
                }
            }else{
                updateUiState { copy(errorMessage = provider.context.getString(R.string.cart_is_empty)) }
            }
        } else {
            updateUiState { copy(errorMessage = provider.context.getString(R.string.you_can_enter_only_one_promotion_code)) }
        }

    }

    private fun showSureDeleteDialog() = viewModelScope.launch {
        emitUiEffect(CartContract.UiEffect.ShowSureDeleteDialog)
    }

    private fun getCartFoods() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        when (val result = cartRepository.getCartFoods(EatzySingleton.currentUsername)) {
            is ResponseState.Success -> {
                updateUiState { copy(isLoading = false, cartFoods = result.result) }
            }

            is ResponseState.Error -> {
                updateUiState { copy(isLoading = false) }
            }
        }
    }

    private fun getCartPrice() = viewModelScope.launch {
        val cartPrice = cartRepository.getCartPrice()
        updateUiState { copy(cartPrice = cartPrice) }
    }

    private fun deleteFood(cartItem: CartItem) = viewModelScope.launch {
        updateUiState { copy(isCartLoading = true) }
        when (val response = cartRepository.deleteFoodFromCart(cartItem)) {
            is ResponseState.Success -> {
                val updatedList = uiState.value.cartFoods.filterNot { it.id == cartItem.id }
                updateUiState {
                    copy(
                        cartFoods = updatedList,
                        isCartLoading = false,
                        cartPrice = (uiState.value.cartPrice - cartItem.price).toInt()
                    )
                }
            }

            is ResponseState.Error -> {
                updateUiState {
                    copy(
                        isCartLoading = false,
                        errorMessage = response.exception.message ?: provider.context.getString(R.string.an_unknown_error_occurred)
                    )
                }
            }
        }
    }

    private fun decreaseFood(cartItem: CartItem) = viewModelScope.launch {
        updateUiState { copy(isCartLoading = true) }
        val updatedItem = cartItem.copy(count = cartItem.count - 1)
        val updatedList = uiState.value.cartFoods.map {
            if (it.id == updatedItem.id) updatedItem else it
        }
        when (val response = cartRepository.addOrIncreaseFoodFromCart(updatedItem)) {
            is ResponseState.Success -> {
                updateUiState {
                    copy(
                        cartFoods = updatedList,
                        isCartLoading = false,
                        cartPrice = (uiState.value.cartPrice - cartItem.price).toInt()
                    )
                }
            }

            is ResponseState.Error -> {
                updateUiState {
                    copy(
                        isCartLoading = false,
                        errorMessage = response.exception.message ?: provider.context.getString(R.string.an_unknown_error_occurred)
                    )
                }
            }
        }
    }

    private fun addFood(cartItem: CartItem) = viewModelScope.launch {
        updateUiState { copy(isCartLoading = true) }
        val updatedItem = cartItem.copy(count = cartItem.count + 1)
        when (cartRepository.addOrIncreaseFoodFromCart(updatedItem)) {
            is ResponseState.Success -> {
                val updatedList = uiState.value.cartFoods.map {
                    if (it.id == updatedItem.id) updatedItem else it
                }
                updateUiState {
                    copy(
                        cartFoods = updatedList,
                        isCartLoading = false,
                        cartPrice = (uiState.value.cartPrice + cartItem.price).toInt()
                    )
                }
            }

            is ResponseState.Error -> {
                updateUiState {
                    copy(
                        isCartLoading = false,
                        errorMessage = provider.context.getString(R.string.an_unknown_error_occurred)
                    )
                }
            }
        }
    }

    private fun clearCart() = viewModelScope.launch {
        updateUiState { copy(isCartLoading = true) }
        when (val response = cartRepository.deleteAllFoodsFromCart(uiState.value.cartFoods)) {
            is ResponseState.Success -> {
                updateUiState {
                    copy(
                        cartPrice = 0,
                        cartFoods = emptyList(),
                        isCartLoading = false
                    )
                }
            }

            is ResponseState.Error -> {
                updateUiState {
                    copy(
                        errorMessage = response.exception.message ?: provider.context.getString(R.string.an_unknown_error_occurred),
                        isCartLoading = false
                    )
                }
            }
        }
    }


    private fun updateUiState(block: CartContract.UiState.() -> CartContract.UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: CartContract.UiEffect) {
        _uiEffect.send(uiEffect)
    }

}