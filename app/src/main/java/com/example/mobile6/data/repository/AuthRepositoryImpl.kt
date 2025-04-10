package com.example.mobile6.data.repository

import com.example.mobile6.data.local.TokenManager
import com.example.mobile6.data.remote.dto.request.RefreshTokenRequest
import com.example.mobile6.data.remote.dto.request.SignInRequest
import com.example.mobile6.data.remote.service.AuthService
import com.example.mobile6.data.remote.util.map
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun signIn(
        login: String,
        password: String
    ): Resource<Boolean> = withContext(Dispatchers.IO) {
        authService.signIn(SignInRequest(login, password))
            .onSuccess { authResponse, message ->
                tokenManager.saveTokens(
                    accessToken = authResponse.accessToken.toString(),
                    refreshToken = authResponse.refreshToken.toString()
                )
            }
            .onError { code, error ->
                tokenManager.clearTokens()
            }
            .onException { e ->
                tokenManager.clearTokens()
            }
            .map {
                true
            }
    }

    override suspend fun refreshToken(): Resource<Boolean> =
        withContext(Dispatchers.IO) {
            val refreshToken = tokenManager.refreshTokenFlow.first()
            Timber.d("Refresh token $refreshToken")
            if (refreshToken.isNullOrBlank()) {
                return@withContext Resource.Error("Refresh token trống")
            }
            authService.refreshToken(RefreshTokenRequest(refreshToken))
                .onSuccess { authResponse, message ->
                    tokenManager.saveTokens(
                        accessToken = authResponse.accessToken.toString(),
                        refreshToken = authResponse.refreshToken.toString()
                    )
                }
                .onError { code, error ->
                    tokenManager.clearTokens()
                }
                .onException { e ->
                    tokenManager.clearTokens()
                }
                .map {
                    true
                }
        }

    override suspend fun signOut(): Resource<Unit> = withContext(Dispatchers.IO) {
        tokenManager.clearTokens()
        Resource.Success(Unit, "Đăng xuất thành công")
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return tokenManager.accessTokenFlow
            .map { accessToken ->
                !accessToken.isNullOrBlank()
            }
    }
}