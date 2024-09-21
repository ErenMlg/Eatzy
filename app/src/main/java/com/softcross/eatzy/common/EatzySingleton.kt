package com.softcross.eatzy.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.softcross.eatzy.domain.model.User

object EatzySingleton {

    var currentUser: User? = null

    var currentUsername: String = currentUser?.username ?: ""

    var cartAmount = mutableIntStateOf(0)


}