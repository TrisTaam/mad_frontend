package com.example.mobile6.data.remote.dto.response

import com.example.mobile6.data.remote.util.DateSerializer
import kotlinx.serialization.Serializable
import java.sql.Date

@Serializable
data class DoctorResponse(
    val id: Long,
    val specialty: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val gender: String? = null,
    @Serializable(with = DateSerializer::class)
    val dateOfBirth: Date? = null,
    val weight: Int? = null,
    val height: Int? = null,
    val role: String? = null
)