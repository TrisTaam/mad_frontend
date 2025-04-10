package com.example.mobile6.data.remote.util

import com.example.mobile6.domain.model.Resource
import kotlinx.serialization.json.Json
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

class ResourceCallAdapterFactory @Inject constructor(
    private val json: Json
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        check(returnType is ParameterizedType) { "Call return type must be parameterized as Call<T>" }
        val responseType = getParameterUpperBound(0, returnType)

        if (getRawType(responseType) != Resource::class.java) {
            return null
        }

        check(responseType is ParameterizedType) { "Resource return type must be parameterized as Resource<T>" }
        val successBodyType = getParameterUpperBound(0, responseType)

        return ResourceCallAdapter<Any>(successBodyType, json)
    }
}
