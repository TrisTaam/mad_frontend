package com.example.mobile6.data.mapper

import com.example.mobile6.data.remote.dto.response.ChatDoctorInfoResponse
import com.example.mobile6.domain.model.Doctor

fun ChatDoctorInfoResponse.toDoctor(): Doctor {
    return Doctor(
        id = id,
        specialty = specialty,
        firstName = firstName,
        lastName = lastName,
        email = null,
        phoneNumber = null,
        gender = null,
        dateOfBirth = null,
        weight = null,
        height = null,
        role = null,
        avatar = null,
        doctorInfos = null
    )
} 