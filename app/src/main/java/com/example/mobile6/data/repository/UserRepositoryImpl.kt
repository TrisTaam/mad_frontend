package com.example.mobile6.data.repository

import com.example.mobile6.data.remote.dto.request.UpdateAdditionalUserInfoRequest
import com.example.mobile6.data.remote.service.UserService
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {

    override suspend fun updateAdditionalInformation(request: UpdateAdditionalUserInfoRequest): Resource<String> =
        withContext(
            Dispatchers.IO
        ) {
            userService.updateAdditionalInformation(request)
                .onSuccess { data, message ->
                    Timber.i("Cập nhật thông tin bổ sung cho người dùng thành công");
                }
                .onError { message, code ->
                    Timber.e("Lỗi khi cập nhật thông tin bổ sung cho người dùng: $message, code: $code")
                }
                .onException { e ->
                    Timber.e(e, "Ngoại lệ khi cập nhật thông tin bổ sung cho người dùng")
                }
        }
}