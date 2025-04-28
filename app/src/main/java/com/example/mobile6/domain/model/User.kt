package com.example.mobile6.domain.model

import java.time.LocalDate

data class User (
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val gender: String,
    val dateOfBirth: LocalDate,
    val weight: Int,
    val height: Int,
)
