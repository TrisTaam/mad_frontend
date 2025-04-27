package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MedicineInteractionResponse(
    val hasInteraction: Boolean,
    val interactionDetails: String
) 