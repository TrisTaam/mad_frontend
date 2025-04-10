package com.example.mobile6.data.remote.interceptor

import com.example.mobile6.data.local.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

const val AUTHORIZATION_HEADER = "Authorization"
const val BEARER_PREFIX = "Bearer "

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (shouldSkipAuth(originalRequest)) {
            return chain.proceed(originalRequest)
        }

        val accessToken = runBlocking { tokenManager.accessTokenFlow.first() }

        if (accessToken.isNullOrBlank()) {
            return chain.proceed(originalRequest)
        }

        val authenticatedRequest = originalRequest.newBuilder()
            .header(AUTHORIZATION_HEADER, "$BEARER_PREFIX$accessToken")
            .build()

        return chain.proceed(authenticatedRequest)
    }

    private fun shouldSkipAuth(request: Request): Boolean {
        val path = request.url.encodedPath
        return path.contains("/auth/sign-in") ||
                path.contains("/auth/sign-up") ||
                path.contains("/auth/refresh-token")
    }
}
