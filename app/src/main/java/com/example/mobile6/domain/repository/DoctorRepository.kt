package com.example.mobile6.domain.repository

import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.domain.model.Resource

interface DoctorRepository {
    suspend fun getSpecialties(): Resource<List<Doctor>>
}