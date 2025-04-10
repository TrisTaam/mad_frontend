package com.example.mobile6.data.remote.interceptor

import com.example.mobile6.data.local.cache.RequestCacheManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheInterceptor @Inject constructor(
    private val requestCacheManager: RequestCacheManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (request.method != "GET") {
            return chain.proceed(request)
        }

        return try {
            val response = chain.proceed(request)
            if (response.isSuccessful) {
                cacheSuccessfulResponse(request, response)
            }
            response
        } catch (e: IOException) {
            Timber.e(e, "Network request failed, trying to use cached response")
            getCachedResponse(request, chain) ?: throw e
        }
    }

    private fun cacheSuccessfulResponse(request: Request, response: Response) {
        val responseBody = response.body ?: return
        val url = request.url.toString()

        try {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer.clone()
            val bodyString = buffer.readUtf8()

            runBlocking {
                requestCacheManager.cacheResponse(url, bodyString)
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to cache response")
        }
    }

    private fun getCachedResponse(request: Request, chain: Interceptor.Chain): Response? {
        val url = request.url.toString()

        return runBlocking {
            val cachedResponseBody = requestCacheManager.getCachedResponse(url)

            if (cachedResponseBody != null) {
                Timber.d("Using cached response for: $url")
                Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK (Cached)")
                    .body(cachedResponseBody.toResponseBody("application/json".toMediaTypeOrNull()))
                    .build()
            } else {
                null
            }
        }
    }
}
