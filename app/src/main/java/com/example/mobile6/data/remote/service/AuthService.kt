package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.request.RefreshTokenRequest
import com.example.mobile6.data.remote.dto.request.SignInRequest
import com.example.mobile6.data.remote.dto.response.AuthResponse
import com.example.mobile6.domain.model.Resource
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/v1/auth/sign-in")
    suspend fun signIn(@Body signInRequest: SignInRequest): Resource<AuthResponse>

    @POST("api/v1/auth/refresh-token")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Resource<AuthResponse>
}