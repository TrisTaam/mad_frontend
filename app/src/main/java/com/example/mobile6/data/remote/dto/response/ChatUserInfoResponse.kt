package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatUserInfoResponse (
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val avatar: String,
    val gender: String,
    val dateOfBirth: String,
    val weight: Int,
    val height: Int,
    val role: String,
    val username: String,
)

