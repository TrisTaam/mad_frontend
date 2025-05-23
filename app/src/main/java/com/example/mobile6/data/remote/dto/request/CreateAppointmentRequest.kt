package com.example.mobile6.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateAppointmentRequest(
    val doctorId: Long,
    val appointmentDate: String
) 