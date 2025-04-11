package com.example.mobile6.data.repository

import com.example.mobile6.data.mapper.toMedicine
import com.example.mobile6.data.remote.service.MedicineService
import com.example.mobile6.domain.model.Medicine
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.MedicineRepository
import javax.inject.Inject

class MedicineRepositoryImpl @Inject constructor(
    private val medicineService: MedicineService
) : MedicineRepository {
    override suspend fun getMedicines(): Resource<List<Medicine>> {
        return try {
            val response = medicineService.getMedicines()
            Resource.Success(response.data.map { it.toMedicine() }, response.message)
        } catch (e: Exception) {
            Resource.Exception(e)
        }
    }

    override suspend fun getMedicineById(id: Long): Resource<Medicine> {
        return try {
            val response = medicineService.getMedicineById(id)
            Resource.Success(response.toMedicine(), "Success")
        } catch (e: Exception) {
            Resource.Exception(e)
        }
    }
} 