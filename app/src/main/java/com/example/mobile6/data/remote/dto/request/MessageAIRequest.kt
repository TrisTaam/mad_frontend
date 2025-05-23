package com.example.mobile6.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class MessageAIRequest(
    val model: String,
    val messages: List<Message>
) {
    @Serializable
    data class Message(
        val role: String,
        val content: String
    )
}