package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MessageAIResponse (
    val message: String,
    val data: String
) {
    val content: String
        get() = data
}