package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class NetworkResponse<T>(
    val message: String,
    val data: T
)
