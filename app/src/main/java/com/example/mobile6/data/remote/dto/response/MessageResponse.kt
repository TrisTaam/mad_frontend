package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    @SerialName("id")
    val id: Long,
    
    @SerialName("senderId")
    val senderId: Long,
    
    @SerialName("receiverId")
    val receiverId: Long,
    
    @SerialName("content")
    val content: String,
    
    @SerialName("sentAt")
    val sentAt: String
)

@Serializable
data class MessageListResponse(
    @SerialName("message")
    val message: String = "",
    
    @SerialName("data")
    val data: List<MessageResponse> = emptyList()
)