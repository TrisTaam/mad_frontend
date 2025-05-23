package com.example.mobile6.data.repository

import com.example.mobile6.data.local.TokenManager
import com.example.mobile6.data.local.pref.Preferences
import com.example.mobile6.data.remote.dto.request.RefreshTokenRequest
import com.example.mobile6.data.remote.dto.request.SignInRequest
import com.example.mobile6.data.remote.dto.request.SignUpRequest
import com.example.mobile6.data.remote.service.AuthService
import com.example.mobile6.data.remote.service.UserService
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
    private val userService: UserService,
    private val tokenManager: TokenManager,
    private val preferences: Preferences
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
                handleCurrentUser()
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

    private suspend fun handleCurrentUser() {
        userService.getDetailInfo()
            .onSuccess { userResponse, message ->
                if (preferences.isDoctorSignedIn) return@onSuccess
                if (userResponse.role == "DOCTOR") {
                    preferences.isDoctorSignedIn = true
                    preferences.isDoctorMode = true
                } else {
                    preferences.isDoctorSignedIn = false
                    preferences.isDoctorMode = false
                }
            }
            .onError { code, error ->
                Timber.e("Lỗi lấy thông tin người dùng: $error - Mã lỗi: $code")
            }
            .onException { e ->
                Timber.e("Lỗi lấy thông tin người dùng: $e")
            }
    }

    override suspend fun signUp(request: SignUpRequest): Resource<Boolean> =
        withContext(Dispatchers.IO) {
            authService.signUp(request)
                .onSuccess { data, message ->
                    Timber.i("Đăng ký thành công: $message")
                }
                .onError { code, error ->
                    Timber.e("Lỗi đăng ký: $error - Mã lỗi: $code")
                }
                .onException { e ->
                    Timber.e("Lỗi đăng ký: $e")
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
        preferences.isDoctorSignedIn = false
        preferences.isDoctorMode = false
        Resource.Success(Unit, "Đăng xuất thành công")
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return tokenManager.accessTokenFlow
            .map { accessToken ->
                !accessToken.isNullOrBlank()
            }
    }

    override suspend fun isDoctorSignedIn(): Boolean = withContext(Dispatchers.IO) {
        preferences.isDoctorSignedIn
    }

    override suspend fun isDoctorMode(): Boolean = withContext(Dispatchers.IO) {
        preferences.isDoctorMode
    }

    override suspend fun changeDoctorMode(isDoctorMode: Boolean) = withContext(Dispatchers.IO) {
        preferences.isDoctorMode = isDoctorMode
    }
}