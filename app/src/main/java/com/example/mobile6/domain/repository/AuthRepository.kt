package com.example.mobile6.domain.repository

import com.example.mobile6.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(login: String, password: String): Resource<Boolean>
    suspend fun refreshToken(): Resource<Boolean>
    suspend fun signOut(): Resource<Unit>
    fun isLoggedIn(): Flow<Boolean>
}