package com.example.mobile6.ui.auth.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.dto.request.SignUpRequest
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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage: SharedFlow<String> = _uiMessage

    private val _firstNameError = MutableLiveData<String?>()
    val firstNameError: LiveData<String?> = _firstNameError

    private val _lastNameError = MutableLiveData<String?>()
    val lastNameError: LiveData<String?> = _lastNameError

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _phoneError = MutableLiveData<String?>()
    val phoneError: LiveData<String?> = _phoneError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    fun validateAndRegister(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        password: String
    ) {
        // Reset errors
        _firstNameError.value = null
        _lastNameError.value = null
        _emailError.value = null
        _phoneError.value = null
        _passwordError.value = null

        var isValid = true

        // Validate first name
        if (firstName.isBlank()) {
            _firstNameError.value = "Vui lòng nhập tên của bạn"
            isValid = false
        }

        // Validate last name
        if (lastName.isBlank()) {
            _lastNameError.value = "Vui lòng nhập họ và tên đệm của bạn"
            isValid = false
        }

        // Validate email
        if (email.isBlank()) {
            _emailError.value = "Vui lòng nhập email"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = "Email không hợp lệ"
            isValid = false
        }

        // Validate phone
        if (phone.isBlank()) {
            _phoneError.value = "Vui lòng nhập số điện thoại"
            isValid = false
        } else if (!isValidPhoneNumber(phone)) {
            _phoneError.value = "Số điện thoại không hợp lệ"
            isValid = false
        }

        // Validate password
        if (password.isBlank()) {
            _passwordError.value = "Vui lòng nhập mật khẩu"
            isValid = false
        } else if (password.length < 6) {
            _passwordError.value = "Mật khẩu phải có ít nhất 6 ký tự"
            isValid = false
        }

        // If all validations pass, register the user
        if (isValid) {
            registerUser(firstName, lastName, email, phone, password)
        }
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val phonePattern = "^[+]?[0-9]{10,13}\$".toRegex()
        return phonePattern.matches(phone.replace(Regex("[-\\s]"), ""))
    }

    private fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        password: String
    ) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val request = SignUpRequest(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    phoneNumber = phone,
                    password = password
                )

                val result = authRepository.signUp(request)
                result
                    .onSuccess { data, message ->
                        _uiMessage.emit("Đăng ký thành công")
                        _registrationSuccess.value = true
                        // Auto sign in
                        authRepository.signIn(email, password)
                    }
                    .onError { message, code ->
                        if (code != 400) {
                            _uiMessage.emit("Đăng ký thất bại")
                        } else {
                            _uiMessage.emit(message)
                        }
                    }
                    .onException {
                        _uiMessage.emit("Đăng ký thất bại")
                    }
            } catch (e: Exception) {
                _uiMessage.emit("Lỗi: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetRegistrationSuccess() {
        _registrationSuccess.value = false
    }
}