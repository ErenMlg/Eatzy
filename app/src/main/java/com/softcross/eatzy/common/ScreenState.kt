package com.softcross.insuranceapp.common

sealed class ScreenState<out T : Any> {
    object Loading : ScreenState<Nothing>()
    data class Success<out T : Any>(val data: T) : ScreenState<T>()
    data class Error(val message: String) : ScreenState<Nothing>()
}
