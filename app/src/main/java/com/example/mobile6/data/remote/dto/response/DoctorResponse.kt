package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class DoctorResponse(
    val id: Long,
    val specialty: String
)