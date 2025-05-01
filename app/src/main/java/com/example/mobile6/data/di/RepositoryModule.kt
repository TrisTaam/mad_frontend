package com.example.mobile6.data.di

import com.example.mobile6.data.repository.AuthRepositoryImpl
import com.example.mobile6.data.repository.MedicineRepositoryImpl
import com.example.mobile6.data.repository.PrescriptionRepositoryImpl
import com.example.mobile6.data.repository.TestRepositoryImpl
import com.example.mobile6.data.repository.UserRepositoryImpl
import com.example.mobile6.domain.repository.AuthRepository
import com.example.mobile6.domain.repository.TestRepository
import com.example.mobile6.domain.repository.MedicineRepository
import com.example.mobile6.domain.repository.PrescriptionRepository
import com.example.mobile6.domain.repository.UserRepository
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

    @Binds
    @Singleton
    fun bindMedicineRepository(
        medicineRepositoryImpl: MedicineRepositoryImpl
    ): MedicineRepository

    @Binds
    @Singleton
    fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    fun bindPrescriptionRepository(
        prescriptionRepositoryImpl: PrescriptionRepositoryImpl
    ): PrescriptionRepository
}