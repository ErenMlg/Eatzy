package com.softcross.eatzy.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class FoodResponse(
    @SerializedName("yemekler")
    val yemekler: List<FoodDto>,
    @SerializedName("success")
    val success: Int
)

data class FoodDto(
    @SerializedName("yemek_id", alternate = ["sepet_yemek_id"])
    val id: Int,
    @SerializedName("yemek_adi")
    val name: String,
    @SerializedName("yemek_resim_adi")
    val imageUrl: String,
    @SerializedName("yemek_fiyat")
    val price: String
)

@Entity(tableName = "favorite_foods")
data class FavoriteFoodDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "favorite_id")
    val favoriteId: Int = 0,
    @ColumnInfo(name = "yemek_id")
    val id: Int,
    @ColumnInfo(name = "kullanici_adi")
    val userName: String
)