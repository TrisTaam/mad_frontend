package com.example.mobile6.presentation.ui.doctor.doctor_specialty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.DoctorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorSpecialtyViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorSpecialtyUiState())
    val uiState: StateFlow<DoctorSpecialtyUiState> = _uiState.asStateFlow()

    init {
        fetchDoctors()
    }

    fun fetchDoctors() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = doctorRepository.getSpecialties()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            doctors = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }

                is Resource.Exception -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.throwable.message ?: "Đã xảy ra lỗi không mong muốn"
                        )
                    }
                }
            }
        }
    }
}

data class DoctorSpecialtyUiState(
    val doctors: List<Doctor> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)