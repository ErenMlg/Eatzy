package com.softcross.eatzy.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val id: Int = 0,
    val name: String = "",
    val image: String = "",
    val price: Double = 0.0,
    var isFavorite: Boolean = false,
    val rate: Double = 0.0
)