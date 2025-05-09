package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class DoctorInfoResponse(
    val id: Long,
    val title: String,
    val description: String,
    val order: Int
) 