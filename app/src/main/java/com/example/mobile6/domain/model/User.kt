package com.example.mobile6.domain.model

import java.time.LocalDate

data class User(
    val id: Long = 0,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val avatar: String = "",
    val gender: String? = null,
    val dateOfBirth: LocalDate? = null,
    val weight: Int? = null,
    val height: Int? = null,
    val role: Role = Role.USER
)

enum class Role {
    ADMIN,
    USER,
    DOCTOR
}