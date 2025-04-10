package com.example.mobile6.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val login: String,
    val password: String
)