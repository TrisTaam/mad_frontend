package com.example.mobile6.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiMessage = MutableSharedFlow<String>(replay = 1)
    val uiMessage = _uiMessage.asSharedFlow()

    fun signOut() {
        viewModelScope.launch {
            val result = authRepository.signOut()

            result
                .onSuccess { authResponse, message ->
                    _uiMessage.emit("Đăng xuất thành công")
                }.onError { message, code ->
                    _uiMessage.emit("Đăng xuất không thành công. Vui lòng thử lại")
                }.onException { throwable ->
                    _uiMessage.emit("Đăng xuất không thành công. Vui lòng thử lại")
                }
        }
    }
}