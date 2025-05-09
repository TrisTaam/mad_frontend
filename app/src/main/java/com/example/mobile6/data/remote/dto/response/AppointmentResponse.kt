package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class AppointmentResponse(
    val id: Long,
    val userId: Long,
    val doctorId: Long,
    val appointmentDate: String = "",
    val status: String = ""
)