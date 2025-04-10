package com.example.mobile6.data.remote.interceptor

import com.example.mobile6.data.local.TokenManager
import com.example.mobile6.data.remote.dto.request.RefreshTokenRequest
import com.example.mobile6.data.remote.service.AuthService
import com.example.mobile6.domain.model.Resource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    @Named("authServiceForRefresh") private val authService: AuthService
) : Authenticator {

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val requestUrl = response.request.url.toString()
        if (requestUrl.contains("refresh-token") || requestUrl.contains("sign-in")) {
            return null
        }

        val refreshToken = runBlocking { tokenManager.refreshTokenFlow.first() } ?: return null

        return runBlocking {
            try {
                val refreshResult = authService.refreshToken(RefreshTokenRequest(refreshToken))

                when (refreshResult) {
                    is Resource.Success -> {
                        val newAccessToken = refreshResult.data.accessToken.toString()
                        val newRefreshToken = refreshResult.data.refreshToken.toString()

                        tokenManager.saveTokens(
                            accessToken = newAccessToken,
                            refreshToken = newRefreshToken
                        )

                        Timber.d("Token refreshed successfully")

                        response.request.newBuilder()
                            .header(AUTHORIZATION_HEADER, "$BEARER_PREFIX$newAccessToken")
                            .build()
                    }

                    else -> {
                        Timber.e("Failed to refresh token: ${refreshResult}")
                        tokenManager.clearTokens()
                        null
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Exception during token refresh")
                tokenManager.clearTokens()
                null
            }
        }
    }
}
