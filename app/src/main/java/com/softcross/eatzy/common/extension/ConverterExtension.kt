package com.softcross.eatzy.common.extension

import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.data.dto.CartDto
import com.softcross.eatzy.domain.model.CartItem
import com.softcross.eatzy.domain.model.Food

fun CartItem.toCartDto(): CartDto = CartDto(
    id = id,
    name = name,
    imageUrl = image,
    price = price.toInt(),
    count = count,
    userName = EatzySingleton.currentUsername
)

fun CartDto.toCartItem(): CartItem = CartItem(
    id = id,
    name = name,
    image = imageUrl,
    price = price.toDouble(),
    count = count
)