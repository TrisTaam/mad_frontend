package com.example.mobile6.data.repository

import com.example.mobile6.data.mapper.toMedicine
import com.example.mobile6.data.mapper.toMedicineInteraction
import com.example.mobile6.data.remote.dto.request.MedicineInteractionRequest
import com.example.mobile6.data.remote.service.MedicineService
import com.example.mobile6.data.remote.util.map
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.domain.model.Medicine
import com.example.mobile6.domain.model.MedicineInteraction
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.MedicineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MedicineRepositoryImpl @Inject constructor(
    private val medicineService: MedicineService
) : MedicineRepository {
    override suspend fun getMedicines(): Resource<List<Medicine>> = withContext(Dispatchers.IO) {
        medicineService.getMedicines()
            .onError { message, code ->
                Timber.e("Lỗi khi lấy danh sách thuốc: $message, code: $code")
            }
            .onException { e ->
                Timber.e(e, "Ngoại lệ khi lấy danh sách thuốc")
            }
            .map { medicines ->
                medicines.map { it.toMedicine() }
            }
    }

    override suspend fun getMedicineById(id: Long): Resource<Medicine> = withContext(Dispatchers.IO) {
        medicineService.getMedicineById(id)
            .onError { message, code ->
                Timber.e("Lỗi khi lấy chi tiết thuốc: $message, code: $code")
            }
            .onException { e ->
                Timber.e(e, "Ngoại lệ khi lấy chi tiết thuốc id: $id")
            }
            .map { it.toMedicine() }
    }
    
    override suspend fun checkMedicineInteractions(
        newMedicineId: Long,
        existingMedicineIds: List<Long>
    ): Resource<MedicineInteraction> = withContext(Dispatchers.IO) {
        val request = MedicineInteractionRequest(newMedicineId, existingMedicineIds)
        medicineService.checkMedicineInteractions(request)
            .onError { message, code ->
                Timber.e("Lỗi khi kiểm tra tương tác thuốc: $message, code: $code")
            }
            .onException { e ->
                Timber.e(e, "Ngoại lệ khi kiểm tra tương tác thuốc id: $newMedicineId")
            }
            .map { it.toMedicineInteraction() }
    }
} 