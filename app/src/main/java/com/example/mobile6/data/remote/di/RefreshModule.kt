package com.example.mobile6.data.remote.di

import com.example.mobile6.BuildConfig
import com.example.mobile6.data.remote.service.AuthService
import com.example.mobile6.data.remote.util.ResourceCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RefreshModule {

    @Provides
    @Singleton
    @Named("refreshOkHttpClient")
    fun provideRefreshOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("refreshRetrofit")
    fun provideRefreshRetrofit(
        @Named("refreshOkHttpClient") client: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .addCallAdapterFactory(ResourceCallAdapterFactory(json))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @Named("authServiceForRefresh")
    fun provideAuthServiceForRefresh(
        @Named("refreshRetrofit") retrofit: Retrofit
    ): AuthService {
        return retrofit.create(AuthService::class.java)
    }
}
