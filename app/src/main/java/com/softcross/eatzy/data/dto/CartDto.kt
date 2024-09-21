package com.softcross.eatzy.data.dto

import com.google.gson.annotations.SerializedName
import com.softcross.eatzy.domain.model.Food

data class CartDto(
    @SerializedName("sepet_yemek_id")
    val id: Int,
    @SerializedName("yemek_adi")
    val name: String,
    @SerializedName("yemek_resim_adi")
    val imageUrl: String,
    @SerializedName("yemek_fiyat")
    val price: Int,
    @SerializedName("yemek_siparis_adet")
    val count: Int,
    @SerializedName("kullanici_adi")
    val userName: String
)

data class CartResponse(
    @SerializedName("sepet_yemekler")
    val cart: List<CartDto> = emptyList(),
    @SerializedName("success")
    val success: Int = 1
)
