package com.example.mobile6.ui.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiMessage = MutableSharedFlow<String>(replay = 1)
    val uiMessage = _uiMessage.asSharedFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.signIn(username, password)

            result
                .onSuccess { authResponse, message ->
                    _isSuccess.update { true }
                    _uiMessage.emit("Đăng nhập thành công")
                }.onError { message, code ->
                    _isSuccess.update { false }
                    _uiMessage.emit("Đăng nhập không thành công. Vui lòng thử lại")
                }.onException { throwable ->
                    _isSuccess.update { false }
                    _uiMessage.emit("Đăng nhập không thành công. Vui lòng thử lại")
                }
        }
    }
}