package com.example.mobile6.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.domain.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListDoctorViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _doctors = MutableLiveData<List<Doctor>>()
    val doctors: LiveData<List<Doctor>> = _doctors

    fun fetchDoctorsForUser(userId: Long) {
        viewModelScope.launch {
            try {
                val list = messageRepository.getAllDoctorsForUser(userId)
                _doctors.value = list
            } catch (e: Exception) {
                _doctors.value = emptyList() // hoặc handle lỗi khác tùy ý
            }
        }
    }
}