package com.example.mobile6.presentation.ui.prescriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.dto.response.PrescriptionResponse
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.repository.AuthRepository
import com.example.mobile6.domain.repository.PrescriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrescriptionsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val prescriptionRepository: PrescriptionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    private var allPrescriptions: List<PrescriptionResponse> = emptyList()

    init {
//        getPrescriptions()
    }

    fun getPrescriptions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val mode = authRepository.isDoctorMode()
            _uiState.update { it.copy(isDoctorMode = mode) }
            val result = if (mode) {
                prescriptionRepository.getPrescriptionsForDoctor()
            } else {
                prescriptionRepository.getPrescriptionsForUser()
            }
            result
                .onSuccess { prescriptions, message ->
                    allPrescriptions = prescriptions
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            prescriptions = prescriptions,
                        )
                    }
                }.onError { message, code ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = message
                        )
                    }
                }.onException { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message
                        )
                    }
                }
        }
    }

    fun search(query: String) {
        val filteredPrescriptions = allPrescriptions.filter { prescription ->
            prescription.prescriptionName
                .contains(query, ignoreCase = true)
        }
        _uiState.update { it.copy(prescriptions = filteredPrescriptions) }
    }

    data class UIState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val isDoctorMode: Boolean = false,
        val prescriptions: List<PrescriptionResponse> = emptyList()
    )
}