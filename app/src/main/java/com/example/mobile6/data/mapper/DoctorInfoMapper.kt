package com.example.mobile6.data.mapper

import com.example.mobile6.data.remote.dto.response.DoctorInfoResponse
import com.example.mobile6.domain.model.DoctorInfo

fun DoctorInfoResponse.toDoctorInfo(): DoctorInfo {
    return DoctorInfo(
        id = id,
        title = title,
        description = description,
        order = order
    )
} 