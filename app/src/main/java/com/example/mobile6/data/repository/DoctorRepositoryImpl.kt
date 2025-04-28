package com.example.mobile6.data.repository

import com.example.mobile6.data.mapper.toDoctor
import com.example.mobile6.data.remote.service.DoctorService
import com.example.mobile6.data.remote.util.map
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.DoctorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DoctorRepositoryImpl @Inject constructor(
    private val doctorService: DoctorService
) : DoctorRepository {
    override suspend fun getSpecialties(): Resource<List<Doctor>> = withContext(Dispatchers.IO) {
        doctorService.getSpecialties()
            .onError { message, code ->
                Timber.e("Lỗi khi lấy danh sách bác sĩ: $message, code: $code")
            }
            .onException { e ->
                Timber.e(e, "Ngoại lệ khi lấy danh sách bác sĩ")
            }
            .map { doctors ->
                doctors.map { it.toDoctor() }
            }
    }
}