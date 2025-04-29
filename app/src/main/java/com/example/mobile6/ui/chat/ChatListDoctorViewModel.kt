package com.example.mobile6.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListDoctorViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _uiMessage = MutableSharedFlow<String>(replay = 1)
    val uiMessage = _uiMessage.asSharedFlow()

    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors = _doctors.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun fetchDoctorsForUser() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = messageRepository.getAllDoctorsForUser()
            result
                .onSuccess { doctors, _ ->
                    _doctors.update { doctors.map { Doctor(it.specialty, it.firstname, it.lastname) } }
                    _uiMessage.emit("Lấy danh sách bác sĩ thành công")
                }
                .onError { message, _ ->
                    _doctors.update { emptyList() }
                    _uiMessage.emit(message)
                }
                .onException { throwable ->
                    _doctors.update { emptyList() }
                    _uiMessage.emit(throwable.message ?: "Có lỗi xảy ra")
                }
            _isLoading.value = false
        }
    }
}