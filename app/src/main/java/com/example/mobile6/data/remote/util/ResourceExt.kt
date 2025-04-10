package com.example.mobile6.data.remote.util

import com.example.mobile6.domain.model.Resource

inline fun <T, R> Resource<T>.onSuccess(block: (T, String) -> R): Resource<T> = apply {
    if (this is Resource.Success) {
        block(data, message)
    }
}

inline fun <T, R> Resource<T>.onError(block: (String, Int?) -> R): Resource<T> = apply {
    if (this is Resource.Error) {
        block(message, code)
    }
}

inline fun <T, R> Resource<T>.onException(block: (Throwable) -> R): Resource<T> = apply {
    if (this is Resource.Exception) {
        block(throwable)
    }
}

inline fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Success -> Resource.Success(transform(data), message)
        is Resource.Error -> this
        is Resource.Exception -> this
    }
}