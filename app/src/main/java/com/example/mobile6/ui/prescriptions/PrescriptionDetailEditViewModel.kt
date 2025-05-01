package com.example.mobile6.ui.prescriptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.R
import com.example.mobile6.data.remote.dto.response.PrescriptionDetailResponse
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.model.Medicine
import com.example.mobile6.domain.repository.MedicineRepository
import com.example.mobile6.domain.repository.PrescriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrescriptionDetailEditViewModel @Inject constructor(
    private val prescriptionRepository: PrescriptionRepository,
    private val medicineRepository: MedicineRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage: SharedFlow<String> = _uiMessage

    private val _isEditMode = MutableLiveData<Boolean>()
    val isEditMode: LiveData<Boolean> = _isEditMode

    private val _prescriptionDetail = MutableLiveData<PrescriptionDetailResponse?>()
    val prescriptionDetail: LiveData<PrescriptionDetailResponse?> = _prescriptionDetail

    private val _selectedMedicine = MutableLiveData<Medicine?>()
    val selectedMedicine: LiveData<Medicine?> = _selectedMedicine

    private val _selectedUnit = MutableLiveData<String>("Viên")
    val selectedUnit: LiveData<String> = _selectedUnit

    private var prescriptionDetailId: Long? = null

    init {
        // Check if we're editing an existing medicine
        savedStateHandle.get<Long>("prescriptionDetailId")?.let { id ->
            prescriptionDetailId = id
            _isEditMode.value = true
            loadPrescriptionDetail(id)
        } ?: run {
            _isEditMode.value = false
        }
    }

    private fun loadPrescriptionDetail(id: Long) {
        viewModelScope.launch {
            try {
                prescriptionRepository.getPrescriptionDetail(id)
                    .onSuccess { data, message ->
                        _prescriptionDetail.value = data
                        _selectedUnit.value = data.quantityUnit
                        medicineRepository.getMedicineById(data.medicineId)
                            .onSuccess { medData, _ ->
                                _selectedMedicine.value = medData
                            }
                    }
                    .onError { message, code ->
                        if (code == 400) {
                            _uiMessage.emit(message)
                        } else {
                            _uiMessage.emit("Không thể tải thông tin thuốc, mã lỗi: $code")
                        }
                    }
            } catch (e: Exception) {
                _uiMessage.emit("Không thể tải thông tin thuốc: ${e.message}")
            }
        }
    }

    fun setSelectedMedicine(medicine: Medicine) {
        _selectedMedicine.value = medicine
    }

    fun setSelectedUnit(unit: String) {
        _selectedUnit.value = unit
    }

    fun savePrescriptionDetail(quantityStr: String, quantityUsage: String): PrescriptionDetailResponse? {
        val selectedMedicine = _selectedMedicine.value
        if (selectedMedicine == null) {
            viewModelScope.launch {
                _uiMessage.emit("Vui lòng chọn thuốc")
            }
            return null
        }

        if (quantityStr.isBlank()) {
            viewModelScope.launch {
                _uiMessage.emit("Vui lòng nhập số lượng")
            }
            return null
        }

        val quantity = try {
            quantityStr.toInt()
        } catch (e: NumberFormatException) {
            viewModelScope.launch {
                _uiMessage.emit("Số lượng không hợp lệ")
            }
            return null
        }

        if (quantity <= 0) {
            viewModelScope.launch {
                _uiMessage.emit("Số lượng phải lớn hơn 0")
            }
            return null
        }

        if (quantityUsage.isBlank()) {
            viewModelScope.launch {
                _uiMessage.emit("Vui lòng nhập liều lượng")
            }
            return null
        }

        val selectedQuantityUnit: String = when (selectedUnit.value.toString().uppercase()) {
            "VIÊN" -> "PILL"
            "VỈ" -> "SACHET"
            "GÓI" -> "PACK"
            "HỘP" -> "BOX"
            else -> {
                viewModelScope.launch {
                    _uiMessage.emit("Vui lòng chọn đơn vị tính thuốc")
                }
                return null
            }
        }

        val prescriptionDetail = PrescriptionDetailResponse(
            medicineId = selectedMedicine.id,
            medicineName = selectedMedicine.name,
            quantity = quantity,
            quantityUnit = selectedQuantityUnit,
            quantityUsage = quantityUsage
        )

        return prescriptionDetail
    }
}
