package com.softcross.eatzy.data.repository

import androidx.lifecycle.MutableLiveData
import com.softcross.eatzy.R
import com.softcross.eatzy.common.ContextProvider
import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.common.extension.mapResponse
import com.softcross.eatzy.common.extension.toCartDto
import com.softcross.eatzy.common.extension.toCartItem
import com.softcross.eatzy.data.source.remote.RemoteDataSource
import com.softcross.eatzy.domain.model.CartItem
import com.softcross.eatzy.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val provider: ContextProvider
) : CartRepository {

    override suspend fun getCartPrice(): Int {
        val cartFoods = remoteDataSource.getCartFoods(EatzySingleton.currentUsername)
        return if (cartFoods is ResponseState.Success) {
            EatzySingleton.cartAmount.value = cartFoods.result.sumOf { (it.count) }
            cartFoods.result.sumOf { (it.price * it.count) }
        } else {
            0
        }
    }

    override suspend fun getCartFoods(username: String): ResponseState<List<CartItem>> =
        remoteDataSource.getCartFoods(username).mapResponse { this.map { it.toCartItem() } }

    override suspend fun deleteAllFoodsFromCart(cartList: List<CartItem>): ResponseState<Unit> {
        val requestCounter = MutableLiveData(0)

        cartList.forEach { cartItem ->
            if (remoteDataSource.deleteFoodFromCart(
                    cartItem.id,
                    EatzySingleton.currentUsername
                ) is ResponseState.Success
            ) {
                requestCounter.value = requestCounter.value?.plus(1)
            }
        }

        if (requestCounter.value == cartList.size) {
            return ResponseState.Success(Unit)
        } else {
            return ResponseState.Error(Exception(provider.context.getString(R.string.error_on_delete_process),))
        }
    }

    override suspend fun deleteFoodFromCart(
        cartItem: CartItem
    ): ResponseState<Unit> {
        val cartFoodsResponse = remoteDataSource.getCartFoods(EatzySingleton.currentUsername)
        when (cartFoodsResponse) {
            is ResponseState.Error -> {
                return ResponseState.Error(cartFoodsResponse.exception)
            }

            is ResponseState.Success -> {
                val cartFoods = cartFoodsResponse.result.find { it.name == cartItem.name }
                if (cartFoods != null) {
                    remoteDataSource.deleteFoodFromCart(
                        cartFoods.id,
                        EatzySingleton.currentUsername
                    )
                }
                return remoteDataSource.deleteFoodFromCart(
                    cartItem.id,
                    EatzySingleton.currentUsername
                )
            }
        }
    }


    override suspend fun addOrIncreaseFoodFromCart(cartItem: CartItem): ResponseState<Unit> {
        val cartFoodsResponse = remoteDataSource.getCartFoods(EatzySingleton.currentUsername)
        when (cartFoodsResponse) {
            is ResponseState.Error -> {
                return ResponseState.Error(cartFoodsResponse.exception)
            }

            is ResponseState.Success -> {
                val cartFoods = cartFoodsResponse.result.find { it.name == cartItem.name }
                if (cartFoods != null) {
                    remoteDataSource.deleteFoodFromCart(
                        cartFoods.id,
                        EatzySingleton.currentUsername
                    )
                }
                return remoteDataSource.addFoodToCart(cartItem.toCartDto())
            }
        }
    }

    override suspend fun addFoodToCart(cartItem: CartItem): ResponseState<Unit> {
        val cartFoodsResponse = remoteDataSource.getCartFoods(EatzySingleton.currentUsername)
        when (cartFoodsResponse) {
            is ResponseState.Error -> {
                return ResponseState.Error(cartFoodsResponse.exception)
            }

            is ResponseState.Success -> {
                val cartFoods = cartFoodsResponse.result.find { it.name == cartItem.name }
                if (cartFoods != null) {
                    remoteDataSource.deleteFoodFromCart(
                        cartFoods.id,
                        EatzySingleton.currentUsername
                    )
                    remoteDataSource.addFoodToCart(
                        cartItem.copy(count = cartItem.count + cartFoods.count).toCartDto()
                    )
                } else {
                    remoteDataSource.addFoodToCart(cartItem.toCartDto())
                }
                return ResponseState.Success(Unit)
            }
        }
    }
}