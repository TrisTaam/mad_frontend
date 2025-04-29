package com.example.mobile6.data.remote.di

import com.example.mobile6.data.remote.service.AuthService
import com.example.mobile6.data.remote.service.MedicineService
import com.example.mobile6.data.remote.service.TestService
import com.example.mobile6.data.remote.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {
    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideTestService(retrofit: Retrofit): TestService {
        return retrofit.create(TestService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideMedicineService(retrofit: Retrofit): MedicineService {
        return retrofit.create(MedicineService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}