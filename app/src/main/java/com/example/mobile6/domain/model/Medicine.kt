package com.example.mobile6.domain.model

data class Medicine(
    val id: Long,
    val name: String,
    val imgUrl: String,
    val description: String,
    val use: String,
    val usage: String,
    val precaution: String,
    val ingredients: List<Ingredient> = emptyList()
)

data class Ingredient(
    val id: Long,
    val name: String,
    val amount: String
)