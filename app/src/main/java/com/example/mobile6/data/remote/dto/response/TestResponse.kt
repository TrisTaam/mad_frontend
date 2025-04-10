package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class TestResponse(
    val id: Long,
    val username: String
)