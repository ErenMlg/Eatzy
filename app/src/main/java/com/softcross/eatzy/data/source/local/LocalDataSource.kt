package com.softcross.eatzy.data.source.local

import com.softcross.eatzy.data.dto.FavoriteFoodDto

interface LocalDataSource {

    suspend fun getFavoriteFoods() : List<FavoriteFoodDto>

    suspend fun setFavoriteFoods(favoriteFoodDto: FavoriteFoodDto)

    suspend fun clearFavoriteFoods()

}