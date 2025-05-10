package com.example.mobile6.ui.prescriptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.dto.response.PrescriptionResponse
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.repository.PrescriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrescriptionScanViewModel @Inject constructor(
    private val prescriptionRepository: PrescriptionRepository
) : ViewModel() {

    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage: SharedFlow<String> = _uiMessage

    private val _prescription = MutableLiveData<PrescriptionResponse>()
    val prescription: LiveData<PrescriptionResponse> = _prescription

    fun fetchPrescription(prescriptionId: Long) {
        viewModelScope.launch {
            prescriptionRepository.getPrescription(prescriptionId)
                .onSuccess { data, message ->
                    _prescription.value = data
                }
                .onError { message, code ->
                    if (code == 400) {
                        _uiMessage.emit("Không thể tìm thấy đơn thuốc")
                    } else {
                        _uiMessage.emit("Có lỗi xảy ra khi tìm đơn thuốc")
                    }
                }
                .onException { e ->
                    _uiMessage.emit("Có lỗi xảy ra khi tìm đơn thuốc")
                }
        }
    }
}