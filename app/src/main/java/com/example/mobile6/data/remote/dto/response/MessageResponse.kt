package com.example.mobile6.data.remote.dto.response
import kotlinx.serialization.Serializable

@Serializable
data class MessageData(
    val id: Long,
    val senderId: Long,
    val receiverId: Long,
    val content: String,
    val sentAt: String
)

@Serializable
data class MessageListResponse(
    val message: String,
    val data: List<MessageData>
)