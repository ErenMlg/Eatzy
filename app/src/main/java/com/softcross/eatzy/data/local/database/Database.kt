package com.softcross.eatzy.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.softcross.eatzy.data.dto.FavoriteFoodDto
import com.softcross.eatzy.data.local.dao.FavoriteFoodsDao

@Database(entities = [FavoriteFoodDto::class], version = 1)
abstract class Database : RoomDatabase() {

    abstract fun getFavoriteFoodsDao(): FavoriteFoodsDao

}
