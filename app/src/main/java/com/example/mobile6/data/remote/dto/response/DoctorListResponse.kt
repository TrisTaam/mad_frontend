package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class DoctorListResponse(
    val id: Long,
    val specialty: String?,
    val firstName: String?,
    val lastName: String?
) 