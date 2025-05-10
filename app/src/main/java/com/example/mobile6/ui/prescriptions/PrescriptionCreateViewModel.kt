package com.example.mobile6.ui.prescriptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.dto.request.CreatePrescriptionDetailRequest
import com.example.mobile6.data.remote.dto.request.CreatePrescriptionRequest
import com.example.mobile6.data.remote.dto.response.PrescriptionDetailResponse
import com.example.mobile6.data.remote.dto.response.PrescriptionResponse
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.repository.PrescriptionRepository
import com.example.mobile6.ui.util.DateUtils.toRequestDateTimeString
import com.example.mobile6.ui.util.DateUtils.toUtilDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PrescriptionCreateViewModel @Inject constructor(
    private val prescriptionRepository: PrescriptionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage: SharedFlow<String> = _uiMessage

    private val _isEditMode = MutableLiveData<Boolean>()
    val isEditMode: LiveData<Boolean> = _isEditMode

    private val _prescription = MutableLiveData<PrescriptionResponse?>()
    val prescription: LiveData<PrescriptionResponse?> = _prescription

    private val _prescriptionDate = MutableLiveData<Date>()
    val prescriptionDate: LiveData<Date> = _prescriptionDate

    private val _prescriptionDetails =
        MutableLiveData<List<PrescriptionDetailResponse>>(emptyList())
    val prescriptionDetail: LiveData<List<PrescriptionDetailResponse>> = _prescriptionDetails

    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    private var _prescriptionId: Long? = null
    val prescriptionId
        get() = _prescriptionId

    init {
        // Check if we're editing an existing prescription
        _prescriptionDate.value = Date()
        savedStateHandle.get<Long>("prescriptionId")?.let { id ->
            _prescriptionId = id
            _isEditMode.value = true
            loadPrescription(id)
        } ?: run {
            _isEditMode.value = false
        }
    }

    private fun loadPrescription(id: Long) {
        viewModelScope.launch {
            prescriptionRepository.getPrescription(id)
                .onSuccess { data, message ->
                    Timber.i("Load đơn thuốc: $id")
                    _prescription.value = data
                    _prescriptionDetails.value = data.details
                }
                .onError { message, code ->
                    if (code == 400) {
                        _uiMessage.emit(message)
                    } else {
                        _uiMessage.emit("Không thể tải đơn thuốc, mã lỗi: $code")
                    }
                }
                .onException { ex ->
                    _uiMessage.emit("Không thể tải đơn thuốc: ${ex.message}")
                }
        }
    }

    fun setPrescriptionDate(date: String) {
        _prescriptionDate.value = date.toUtilDate()
    }

    fun addPrescriptionDetail(prescriptionDetail: PrescriptionDetailResponse) {
        val currentList = _prescriptionDetails.value?.toMutableList() ?: mutableListOf()
        currentList.add(prescriptionDetail)
        _prescriptionDetails.value = currentList
    }

    fun updatePrescriptionDetail(prescriptionDetail: PrescriptionDetailResponse) {
        val currentList = _prescriptionDetails.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.medicineId == prescriptionDetail.medicineId }
        if (index != -1) {
            currentList[index] = prescriptionDetail
            _prescriptionDetails.value = currentList
        }
    }

    fun removePrescriptionDetail(prescriptionDetail: PrescriptionDetailResponse) {
        val currentList = _prescriptionDetails.value?.toMutableList() ?: mutableListOf()
        currentList.remove(prescriptionDetail)
        _prescriptionDetails.value = currentList
    }

    fun savePrescription(name: String, notes: String) {
        if (name.isBlank()) {
            viewModelScope.launch {
                _uiMessage.emit("Vui lòng nhập tên đơn thuốc")
            }
            return
        }

        if (_prescriptionDetails.value.isNullOrEmpty()) {
            viewModelScope.launch {
                _uiMessage.emit("Vui lòng thêm ít nhất một loại thuốc")
            }
            return
        }

        val date =
            _prescriptionDate.value?.toRequestDateTimeString() ?: Date().toRequestDateTimeString()
        val medicines = _prescriptionDetails.value ?: emptyList()

        val request = CreatePrescriptionRequest(
            prescriptionName = name,
            prescriptionDate = date,
            notes = notes,
            details = medicines.map {
                CreatePrescriptionDetailRequest(
                    medicineId = it.medicineId,
                    quantity = it.quantity,
                    quantityUnit = it.quantityUnit.uppercase(),
                    quantityUsage = it.quantityUsage
                )
            }
        )

        viewModelScope.launch {
            try {
                if (_isEditMode.value != true) {
                    prescriptionRepository.createPrescription(request)
                        .onSuccess { data, message ->
                            _prescriptionId = data.prescriptionId
                        }
                }
                _saveSuccess.value = true
            } catch (e: Exception) {
                _uiMessage.emit("Lỗi: ${e.message}")
                Timber.e(e)
            }
        }
    }

    fun resetSaveSuccess() {
        _saveSuccess.value = false
    }
}