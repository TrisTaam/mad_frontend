package com.example.mobile6.data.di

import com.example.mobile6.data.repository.AppointmentRepositoryImpl
import com.example.mobile6.data.repository.AuthRepositoryImpl
import com.example.mobile6.data.repository.DoctorRepositoryImpl
import com.example.mobile6.data.repository.MedicineRepositoryImpl
import com.example.mobile6.data.repository.MessageRepositoryImpl
import com.example.mobile6.data.repository.PrescriptionRepositoryImpl
import com.example.mobile6.data.repository.TestRepositoryImpl
import com.example.mobile6.data.repository.UserAlarmLogRepositoryImpl
import com.example.mobile6.data.repository.UserAlarmRepositoryImpl
import com.example.mobile6.data.repository.UserRepositoryImpl
import com.example.mobile6.domain.repository.AppointmentRepository
import com.example.mobile6.domain.repository.AuthRepository
import com.example.mobile6.domain.repository.DoctorRepository
import com.example.mobile6.domain.repository.MedicineRepository
import com.example.mobile6.domain.repository.MessageRepository
import com.example.mobile6.domain.repository.PrescriptionRepository
import com.example.mobile6.domain.repository.TestRepository
import com.example.mobile6.domain.repository.UserAlarmLogRepository
import com.example.mobile6.domain.repository.UserAlarmRepository
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
    fun bindDoctorRepository(
        doctorRepositoryImpl: DoctorRepositoryImpl
    ): DoctorRepository

    @Binds
    @Singleton
    fun bindAppointmentRepository(
        appointmentRepositoryImpl: AppointmentRepositoryImpl
    ): AppointmentRepository

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

    @Binds
    @Singleton
    fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository

    @Binds
    @Singleton
    fun bindUserAlarmRepository(
        userAlarmRepositoryImpl: UserAlarmRepositoryImpl
    ): UserAlarmRepository

    @Binds
    @Singleton
    fun bindUserAlarmLogRepository(
        userAlarmLogRepositoryImpl: UserAlarmLogRepositoryImpl
    ): UserAlarmLogRepository
}