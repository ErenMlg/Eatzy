package com.softcross.eatzy.domain.repository

import com.softcross.eatzy.data.dto.CartDto
import com.softcross.eatzy.domain.model.Food
import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.data.dto.FavoriteFoodDto
import com.softcross.eatzy.domain.model.CartItem

interface FoodRepository {

    suspend fun getAllFoods(): ResponseState<List<Food>>

    suspend fun getAllFavorites(): ResponseState<List<Food>>

    suspend fun setFavoriteFoods(favoriteFoodDto: FavoriteFoodDto)

    suspend fun clearAllFavorites()

}