package com.example.mobile6.domain.model

data class ChatAIMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
