package com.example.mobile6.data.di

import com.example.mobile6.data.repository.AuthRepositoryImpl
import com.example.mobile6.data.repository.TestRepositoryImpl
import com.example.mobile6.domain.repository.AuthRepository
import com.example.mobile6.domain.repository.TestRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    fun bindTestRepository(
        testRepositoryImpl: TestRepositoryImpl
    ): TestRepository
}