package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatDoctorInfoResponse (
    val id: Long,
    val specialty: String? = null,
    val firstName: String? = null,
    val lastName: String? = null
)