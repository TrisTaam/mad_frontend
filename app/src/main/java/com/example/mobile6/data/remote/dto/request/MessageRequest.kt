package com.example.mobile6.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class MessageRequest(

    val receiverId: Long,
    val content: String,
)