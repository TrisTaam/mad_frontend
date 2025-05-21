package com.example.mobile6.data.remote.di

import com.example.mobile6.data.remote.service.AppointmentService
import com.example.mobile6.data.remote.service.AuthService
import com.example.mobile6.data.remote.service.DoctorService
import com.example.mobile6.data.remote.service.MedicineService
import com.example.mobile6.data.remote.service.MessageService
import com.example.mobile6.data.remote.service.PrescriptionService
import com.example.mobile6.data.remote.service.TestService
import com.example.mobile6.data.remote.service.UserAlarmLogService
import com.example.mobile6.data.remote.service.UserAlarmService
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

    @Provides
    @Singleton
    fun providePrescriptionService(retrofit: Retrofit): PrescriptionService {
        return retrofit.create(PrescriptionService::class.java)
    }

    @Provides
    @Singleton
    fun provideDoctorService(retrofit: Retrofit): DoctorService {
        return retrofit.create(DoctorService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppointmentService(retrofit: Retrofit): AppointmentService {
        return retrofit.create(AppointmentService::class.java)
    }

    @Provides
    @Singleton
    fun provideMessageService(retrofit: Retrofit): MessageService {
        return retrofit.create(MessageService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserAlarmService(retrofit: Retrofit): UserAlarmService {
        return retrofit.create(UserAlarmService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserAlarmLogService(retrofit: Retrofit): UserAlarmLogService {
        return retrofit.create(UserAlarmLogService::class.java)
    }
}