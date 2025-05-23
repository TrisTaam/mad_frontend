package com.example.mobile6.presentation.ui.auth.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage: SharedFlow<String> = _uiMessage

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    fun validateAndLogin(emailOrPhone: String, password: String) {
        // Reset errors
        _emailError.value = null
        _passwordError.value = null

        var isValid = true

        // Validate email or phone
        if (emailOrPhone.isBlank()) {
            _emailError.value = "Vui lòng nhập email hoặc số điện thoại"
            isValid = false
        } else if (!isValidEmailOrPhone(emailOrPhone)) {
            _emailError.value = "Email hoặc số điện thoại không hợp lệ"
            isValid = false
        }

        // Validate password
        if (password.isBlank()) {
            _passwordError.value = "Vui lòng nhập mật khẩu"
            isValid = false
        }

        // If all validations pass, login the user
        if (isValid) {
            loginUser(emailOrPhone, password)
        }
    }

    private fun isValidEmailOrPhone(input: String): Boolean {
        val isEmail = Patterns.EMAIL_ADDRESS.matcher(input).matches()
        val isPhone = input.replace(Regex("[-\\s]"), "").matches(Regex("^[+]?[0-9]{10,13}\$"))
        return isEmail || isPhone
    }

    private fun loginUser(emailOrPhone: String, password: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val result = authRepository.signIn(emailOrPhone, password)

                result
                    .onSuccess { data, message ->
                        _uiMessage.emit("Đăng nhập thành công")
                        _loginSuccess.value = true
                    }
                    .onError { message, code ->
                        if (code == 403) {
                            _uiMessage.emit(message)
                        } else {
                            _uiMessage.emit("Đăng nhập thất bại")
                        }
                    }
                    .onException { ex ->
                        _uiMessage.emit("Đăng nhập thất bại")
                    }
            } catch (e: Exception) {
                _uiMessage.emit("Lỗi: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetLoginSuccess() {
        _loginSuccess.value = false
    }
}