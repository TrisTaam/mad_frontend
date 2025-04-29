package com.example.mobile6.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val testRepository: TestRepository
) : ViewModel() {
    private val _testMessage = MutableSharedFlow<String>()
    val testMessage = _testMessage.asSharedFlow()

    fun test3() {
        viewModelScope.launch {
            val result = testRepository.test3()

            var tmpMessage = ""
            result
                .onSuccess { test, message ->
                    tmpMessage = "Test3 success ${test.id} ${test.username} $message"
                }.onError { message, code ->
                    tmpMessage = "Test3 error $message $code"
                }.onException { throwable ->
                    tmpMessage = "Test3 exception ${throwable.message}"
                }

            _testMessage.emit(tmpMessage)
        }
    }
}