package com.example.mobile6.data.di

import com.example.mobile6.data.repository.AuthRepositoryImpl
import com.example.mobile6.data.repository.TestRepositoryImpl
import com.example.mobile6.data.repository.MedicineRepositoryImpl
import com.example.mobile6.domain.repository.AuthRepository
import com.example.mobile6.domain.repository.TestRepository
import com.example.mobile6.domain.repository.MedicineRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTestRepository(
        testRepositoryImpl: TestRepositoryImpl
    ): TestRepository

    @Binds
    @Singleton
    abstract fun bindMedicineRepository(
        medicineRepositoryImpl: MedicineRepositoryImpl
    ): MedicineRepository
}