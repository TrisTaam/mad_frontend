package com.example.mobile6.ui.chat.doctor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class ChatListDoctorUiState(
    val doctors: List<Doctor> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChatListDoctorViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListDoctorUiState())
    val uiState: StateFlow<ChatListDoctorUiState> = _uiState.asStateFlow()

    private var allDoctors: List<Doctor> = emptyList()

    init {
        fetchDoctorsForUser()
    }

    fun fetchDoctorsForUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            Timber.d("Starting to fetch doctors")

            when (val result = messageRepository.getAllDoctorsForUser()) {
                is Resource.Success -> {
                    Timber.d("Raw doctors data: ${result.data}")
                    if (result.data.isEmpty()) {
                        Timber.d("Danh sách bác sĩ rỗng!")
                    }

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
                    Timber.d("Lỗi lấy danh sách bác sĩ: ${result.message}")
                    _uiState.update {
                        it.copy(
                            doctors = emptyList(),
                            isLoading = false,
                            error = result.message
                        )
                    }
                }

                is Resource.Exception -> {
                    Timber.d("Exception: ${result.throwable.message}")
                    _uiState.update {
                        it.copy(
                            doctors = emptyList(),
                            isLoading = false,
                            error = result.throwable.message ?: "Có lỗi xảy ra"
                        )
                    }
                }
            }
        }
    }

    fun search(query: String) {
        val filteredDoctors = allDoctors.filter { doctor ->
            doctor.firstName?.contains(query, ignoreCase = true) == true ||
                    doctor.lastName?.contains(query, ignoreCase = true) == true
        }
        _uiState.update { it.copy(doctors = filteredDoctors) }
    }
}