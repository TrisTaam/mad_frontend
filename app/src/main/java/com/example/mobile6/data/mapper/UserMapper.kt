package com.example.mobile6.data.mapper

import com.example.mobile6.data.remote.dto.response.UserResponse
import com.example.mobile6.domain.model.Role
import com.example.mobile6.domain.model.User
import java.time.LocalDate

fun UserResponse.toUser(): User {
    return User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        avatar = avatar ?: "",
        gender = gender,
        dateOfBirth = dateOfBirth?.let { LocalDate.parse(it) },
        weight = weight,
        height = height,
        role = when (role) {
            "ADMIN" -> Role.ADMIN
            "DOCTOR" -> Role.DOCTOR
            else -> Role.USER
        }
    )
}

fun User.toUserResponse(): UserResponse {
    return UserResponse(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        avatar = avatar,
        gender = gender,
        dateOfBirth = dateOfBirth?.toString(),
        weight = weight,
        height = height,
        role = when (role) {
            Role.ADMIN -> "ADMIN"
            Role.DOCTOR -> "DOCTOR"
            Role.USER -> "USER"
        }
    )
}
