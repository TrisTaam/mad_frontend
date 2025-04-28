package com.example.mobile6.domain.model

import java.time.LocalDateTime

data class Message(
    val id: Long,
    val senderId: Long,
    val receiverId: Long,
    val content: String,
    val sentAt: LocalDateTime,
)