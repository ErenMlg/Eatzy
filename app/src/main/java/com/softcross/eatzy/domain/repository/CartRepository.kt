package com.softcross.eatzy.domain.repository

import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.domain.model.CartItem

interface CartRepository {

    suspend fun getCartPrice(): Int

    suspend fun getCartFoods(username: String): ResponseState<List<CartItem>>

    suspend fun deleteAllFoodsFromCart(cartList: List<CartItem>): ResponseState<Unit>

    suspend fun deleteFoodFromCart(cartItem: CartItem): ResponseState<Unit>

    suspend fun addOrIncreaseFoodFromCart(cartItem: CartItem): ResponseState<Unit>

    suspend fun addFoodToCart(cartItem: CartItem): ResponseState<Unit>


}