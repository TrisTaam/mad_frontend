package com.example.mobile6.data.repository

import com.example.mobile6.data.mapper.toUser
import com.example.mobile6.data.remote.dto.request.UpdateAdditionalUserInfoRequest
import com.example.mobile6.data.remote.service.UserService
import com.example.mobile6.data.remote.util.map
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.model.User
import com.example.mobile6.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
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

    override suspend fun updateAvatar(imageFile: File): Resource<String> =
        withContext(Dispatchers.IO) {
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

            userService.updateAvatar(body)
                .onSuccess { data, message ->
                    Timber.i("Cập nhật avatar thành công")
                }
                .onError { message, code ->
                    Timber.e("Lỗi khi cập nhật avatar: $message, code: $code")
                }
                .onException { e ->
                    Timber.e(e, "Ngoại lệ khi cập nhật avatar")
                }
        }

    override suspend fun getDetailInfo(): Resource<User> =
        withContext(Dispatchers.IO) {
            userService.getDetailInfo()
                .onSuccess { data, message ->
                    Timber.i("Lấy thông tin người dùng thành công")
                }
                .onError { message, code ->
                    Timber.e("Lỗi khi lấy thông tin người dùng: $message, code: $code")
                }
                .onException { e ->
                    Timber.e(e, "Ngoại lệ khi lấy thông tin người dùng")
                }
                .map { it.toUser() }
        }
}