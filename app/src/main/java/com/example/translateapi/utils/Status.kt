package com.example.translateapi.utils

sealed class Status<out T>{
    object Loading: Status<Nothing>()
    data class Success<T>(val data: T): Status<T>()
    data class Error(val message: String): Status<Nothing>()
}