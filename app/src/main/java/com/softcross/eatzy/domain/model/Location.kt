package com.softcross.eatzy.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val id: String,
    val city: String,
    val district: String,
    val country: String,
    val openAddress: String,
    val title: String
)
