package com.softcross.eatzy.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    val id: String,
    val title: String,
    val cartNumber: String,
    val cartHolderName: String,
    val cartExpiryDate: String,
    val cartCVC: String
)
