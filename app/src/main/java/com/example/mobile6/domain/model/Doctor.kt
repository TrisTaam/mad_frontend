package com.example.mobile6.domain.model

import java.sql.Date

data class Doctor(
    val id: Long,
    val specialty: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val phoneNumber: String?,
    val gender: String?,
    val dateOfBirth: Date?,
    val weight: Int?,
    val height: Int?,
    val role: String?
)