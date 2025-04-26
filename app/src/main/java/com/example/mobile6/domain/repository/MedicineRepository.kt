package com.example.mobile6.domain.repository

import com.example.mobile6.domain.model.Medicine
import com.example.mobile6.domain.model.Resource

interface MedicineRepository {
    suspend fun getMedicines(): Resource<List<Medicine>>
    suspend fun getMedicineById(id: Long): Resource<Medicine>
} 