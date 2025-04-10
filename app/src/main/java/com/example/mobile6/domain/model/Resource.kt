package com.example.mobile6.domain.model

sealed class Resource<out T> {
    data class Success<out T>(val data: T, val message: String) : Resource<T>()
    data class Error(val message: String, val code: Int? = null) : Resource<Nothing>()
    data class Exception(val throwable: Throwable) : Resource<Nothing>()
}