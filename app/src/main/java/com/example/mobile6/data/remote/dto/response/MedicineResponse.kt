package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MedicineListResponse(
    val message: String,
    val data: List<MedicineResponse>
)

@Serializable
data class MedicineResponse(
    val id: Long,
    val name: String,
    val imgUrl: String,
    val description: String,
    val use: String? = null,
    val usage: String,
    val precaution: String,
    val ingredients: List<IngredientResponse> = emptyList()
)

@Serializable
data class IngredientResponse(
    val id: Long,
    val name: String,
    val amount: String
) 