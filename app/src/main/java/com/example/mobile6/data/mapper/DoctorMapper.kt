package com.example.mobile6.data.mapper

import com.example.mobile6.data.remote.dto.response.DoctorResponse
import com.example.mobile6.domain.model.Doctor

fun DoctorResponse.toDoctor(): Doctor {
    return Doctor(
        id = id,
        specialty = specialty
    )
}