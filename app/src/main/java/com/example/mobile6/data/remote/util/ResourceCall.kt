package com.example.mobile6.data.remote.util

import com.example.mobile6.data.remote.dto.response.NetworkResponse
import com.example.mobile6.domain.model.Resource
import kotlinx.serialization.json.Json
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.lang.reflect.Type

class ResourceCall<T>(
    private val delegate: Call<NetworkResponse<T>>,
    private val successBodyType: Type,
    private val json: Json
) : Call<Resource<T>> {

    override fun enqueue(callback: Callback<Resource<T>>) {
        delegate.enqueue(object : Callback<NetworkResponse<T>> {
            override fun onResponse(
                call: Call<NetworkResponse<T>>,
                response: Response<NetworkResponse<T>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val resource = Resource.Success(body.data, body.message)
                        callback.onResponse(
                            this@ResourceCall,
                            Response.success(resource)
                        )
                    } else {
                        val resource = Resource.Error("Kết quả trả về không hợp lệ")
                        callback.onResponse(
                            this@ResourceCall,
                            Response.success(resource)
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (!errorBody.isNullOrEmpty()) {
                        try {
                            val errorJson =
                                json.decodeFromString<NetworkResponse<String?>>(errorBody)
                            errorJson.message
                        } catch (e: Exception) {
                            Timber.e(e, "Failed to parse error response")
                            errorBody
                        }
                    } else {
                        when (response.code()) {
                            401 -> "Không được phép truy cập. Vui lòng đăng nhập lại."
                            403 -> "Bạn không có quyền truy cập tài nguyên này."
                            404 -> "Không tìm thấy tài nguyên."
                            500 -> "Lỗi máy chủ. Vui lòng thử lại sau."
                            else -> "Lỗi không xác định (${response.code()})"
                        }
                    }
                    val resource = Resource.Error(errorMessage, response.code())
                    callback.onResponse(
                        this@ResourceCall,
                        Response.success(resource)
                    )
                }
            }

            override fun onFailure(call: Call<NetworkResponse<T>>, t: Throwable) {
                Timber.e(t, "API call failed")
                val resource = when (t) {
                    is IOException -> Resource.Error("Lỗi mạng. Vui lòng kiểm tra kết nối của bạn.")
                    else -> Resource.Exception(t)
                }
                callback.onResponse(
                    this@ResourceCall,
                    Response.success(resource)
                )
            }
        })
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun clone(): Call<Resource<T>> =
        ResourceCall(delegate.clone(), successBodyType, json)

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<Resource<T>> {
        throw UnsupportedOperationException("ResourceCall doesn't support synchronous execution")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}