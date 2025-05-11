package com.example.mobile6.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageRequest(
    @SerialName("receiverId")
    val receiverId: Long,

    @SerialName("content")
    val content: String
)