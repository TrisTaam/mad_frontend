package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
)