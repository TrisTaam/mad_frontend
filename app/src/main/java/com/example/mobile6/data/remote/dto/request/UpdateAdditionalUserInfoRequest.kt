package com.example.mobile6.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAdditionalUserInfoRequest(
    val gender: String,
    val dateOfBirth: String,
    val weight: Int,
    val height: Int
)