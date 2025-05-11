package com.example.mobile6.data.di

import android.content.Context
import com.example.mobile6.data.local.TokenManager
import com.example.mobile6.data.local.cache.RequestCacheManager
import com.example.mobile6.data.local.pref.Preferences
import com.example.mobile6.data.local.room.dao.RequestDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    private const val PREF_NAME = "prefs"

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
        }
    }

    @Provides
    @Singleton
    fun provideTokenManager(
        @ApplicationContext context: Context
    ): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideRequestCacheManager(
        requestDao: RequestDao
    ): RequestCacheManager {
        return RequestCacheManager(requestDao)
    }

    @Provides
    @Singleton
    fun providePreferences(
        @ApplicationContext context: Context
    ): Preferences {
        return Preferences(context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE))
    }
}