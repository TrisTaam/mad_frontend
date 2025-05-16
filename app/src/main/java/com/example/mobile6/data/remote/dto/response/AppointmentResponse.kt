package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class AppointmentResponse(
    val id: Long,
    val userId: Long,
    val userName: String = "",
    val doctorId: Long,
    val doctorName: String = "",
    val appointmentDate: String = "",
    val status: String = ""
)