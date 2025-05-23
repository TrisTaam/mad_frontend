package com.example.mobile6.presentation.ui.doctor.doctor_list

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
class DoctorListViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorListUiState())
    val uiState: StateFlow<DoctorListUiState> = _uiState.asStateFlow()

    private var allDoctors = emptyList<Doctor>()

    fun fetchDoctorsBySpecialty(specialty: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = doctorRepository.getDoctorsBySpecialty(specialty)) {
                is Resource.Success -> {
                    allDoctors = result.data
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

    fun searchDoctors(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(doctors = allDoctors) }
            return
        }

        val filteredList = allDoctors.filter {
            val fullName = "${it.lastName.orEmpty()} ${it.firstName.orEmpty()}"
            fullName.contains(query, ignoreCase = true) || it.specialty?.contains(
                query,
                ignoreCase = true
            ) == true
        }

        _uiState.update { it.copy(doctors = filteredList) }
    }

}

data class DoctorListUiState(
    val doctors: List<Doctor> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)