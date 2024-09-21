package com.softcross.eatzy.data.repository

import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.common.ResponseState
import com.softcross.eatzy.common.extension.mapResponse
import com.softcross.eatzy.data.dto.FavoriteFoodDto
import com.softcross.eatzy.data.source.local.LocalDataSource
import com.softcross.eatzy.data.source.remote.RemoteDataSource
import com.softcross.eatzy.domain.model.Food
import com.softcross.eatzy.domain.repository.FoodRepository
import java.math.RoundingMode
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : FoodRepository {

    override suspend fun getAllFoods(): ResponseState<List<Food>> {
        return try {
            val favoriteFoods = localDataSource.getFavoriteFoods()
                .filter { it.userName == EatzySingleton.currentUsername }
                .map { it.id }
            return remoteDataSource.getAllFoods().mapResponse {
                this.map { food ->
                    Food(
                        id = food.id,
                        name = food.name,
                        image = food.imageUrl,
                        price = food.price.toDouble(),
                        isFavorite = food.id in favoriteFoods,
                        rate = (Math.random() * 5).toBigDecimal().setScale(2, RoundingMode.HALF_UP)
                            .toDouble()
                    )
                }
            }
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun getAllFavorites(): ResponseState<List<Food>> {
        return try {
            val favoriteFoods = localDataSource.getFavoriteFoods()
                .filter { it.userName == EatzySingleton.currentUsername }
                .map { it.id }
            return remoteDataSource.getAllFoods().mapResponse {
                this.map { food ->
                    Food(
                        id = food.id,
                        name = food.name,
                        image = food.imageUrl,
                        price = food.price.toDouble(),
                        isFavorite = food.id in favoriteFoods,
                        rate = (Math.random() * 5).toBigDecimal().setScale(2, RoundingMode.HALF_UP)
                            .toDouble()
                    )
                }.filter { it.isFavorite }
            }
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun setFavoriteFoods(favoriteFoodDto: FavoriteFoodDto) {
        localDataSource.setFavoriteFoods(favoriteFoodDto)
    }

    override suspend fun clearAllFavorites() {
        localDataSource.clearFavoriteFoods()
    }
}