package com.example.mobile6.data.local.cache

import com.example.mobile6.data.local.room.dao.RequestDao
import com.example.mobile6.data.local.room.entity.RequestEntity
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RequestCacheManager @Inject constructor(
    private val requestDao: RequestDao
) {
    companion object {
        private const val DEFAULT_CACHE_EXPIRATION_HOURS = 24L
    }

    suspend fun getCachedResponse(url: String): String? {
        val cachedRequest = requestDao.getCachedResponse(url) ?: return null

        val now = Date()
        val cacheAge = now.time - cachedRequest.cachedAt.time
        val cacheExpirationMillis = TimeUnit.HOURS.toMillis(DEFAULT_CACHE_EXPIRATION_HOURS)

        return if (cacheAge > cacheExpirationMillis) {
            requestDao.deleteOldCache(getExpirationDate())
            null
        } else {
            cachedRequest.response
        }
    }

    suspend fun cacheResponse(url: String, response: String) {
        val requestEntity = RequestEntity(
            request = url,
            response = response,
            cachedAt = Date()
        )
        requestDao.saveResponse(requestEntity)
    }

    suspend fun clearExpiredCache() {
        requestDao.deleteOldCache(getExpirationDate())
    }

    suspend fun clearAllCache() {
        requestDao.clearCache()
    }

    private fun getExpirationDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, -DEFAULT_CACHE_EXPIRATION_HOURS.toInt())
        return calendar.time
    }
}
