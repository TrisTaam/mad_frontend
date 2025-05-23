package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class DoctorResponse(
    val id: Long,
    val specialty: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val gender: String? = null,
    val dateOfBirth: String? = null,
    val weight: Int? = null,
    val height: Int? = null,
    val role: String? = null,
    val avatar: String? = null,
    val doctorInfos: List<DoctorInfoResponse>? = null
)