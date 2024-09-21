package com.softcross.eatzy.data.source.local

import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.data.dto.FavoriteFoodDto
import com.softcross.eatzy.data.local.dao.FavoriteFoodsDao
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val favoriteFoodsDao: FavoriteFoodsDao
) : LocalDataSource {

    override suspend fun getFavoriteFoods(): List<FavoriteFoodDto> {
        return favoriteFoodsDao.getFavoriteFoods()
    }

    override suspend fun setFavoriteFoods(favoriteFoodDto: FavoriteFoodDto) {
        val findedItem = favoriteFoodsDao.getFavoriteFoodById(
            id = favoriteFoodDto.id,
            userId = EatzySingleton.currentUsername
        )
        return if (findedItem == null) {
            favoriteFoodsDao.insertFavoriteFood(favoriteFoodDto)
        } else {
            favoriteFoodsDao.deleteFavoriteFood(favoriteFoodDto.copy(favoriteId = findedItem.favoriteId))
        }
    }

    override suspend fun clearFavoriteFoods() {
        favoriteFoodsDao.deleteFavoriteFood(EatzySingleton.currentUsername)
    }
}