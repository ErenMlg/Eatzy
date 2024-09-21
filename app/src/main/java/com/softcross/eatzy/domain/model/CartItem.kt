package com.softcross.eatzy.domain.model

data class CartItem(
    val id: Int,
    val name: String,
    val image: String,
    val price: Double,
    val count: Int
)