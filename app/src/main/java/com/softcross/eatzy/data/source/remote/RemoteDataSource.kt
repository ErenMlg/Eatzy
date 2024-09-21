package com.softcross.eatzy.data.source.remote

import com.softcross.eatzy.data.dto.CartDto
import com.softcross.eatzy.data.dto.FoodDto
import com.softcross.eatzy.common.ResponseState

interface RemoteDataSource {

    suspend fun getAllFoods(): ResponseState<List<FoodDto>>

    suspend fun addFoodToCart(cartDto: CartDto) : ResponseState<Unit>

    suspend fun getCartFoods(username: String): ResponseState<List<CartDto>>

    suspend fun deleteFoodFromCart(foodCartID: Int, username: String): ResponseState<Unit>

}