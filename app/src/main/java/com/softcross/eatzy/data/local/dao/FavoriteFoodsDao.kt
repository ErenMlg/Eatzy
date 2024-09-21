package com.softcross.eatzy.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.softcross.eatzy.data.dto.FavoriteFoodDto

@Dao
interface FavoriteFoodsDao {

    @Query("SELECT * FROM favorite_foods")
    suspend fun getFavoriteFoods(): List<FavoriteFoodDto>

    @Insert
    suspend fun insertFavoriteFood(favoriteFoodDto: FavoriteFoodDto)


    @Query("SELECT * FROM favorite_foods WHERE yemek_id = :id AND kullanici_adi = :userId")
    suspend fun getFavoriteFoodById(id: Int, userId:String): FavoriteFoodDto?

    @Delete
    suspend fun deleteFavoriteFood(favoriteFoodDto: FavoriteFoodDto)

    @Query("DELETE FROM favorite_foods WHERE kullanici_adi = :userId")
    suspend fun deleteFavoriteFood(userId: String)

}