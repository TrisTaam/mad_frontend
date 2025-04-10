package com.example.mobile6.data.remote.util

import com.example.mobile6.data.remote.dto.response.NetworkResponse
import com.example.mobile6.domain.model.Resource
import kotlinx.serialization.json.Json
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResourceCallAdapter<T>(
    private val successBodyType: Type,
    private val json: Json
) : CallAdapter<NetworkResponse<T>, Call<Resource<T>>> {

    override fun responseType(): Type =
        object : ParameterizedType {
            override fun getActualTypeArguments(): Array<Type> = arrayOf(successBodyType)
            override fun getRawType(): Type = NetworkResponse::class.java
            override fun getOwnerType(): Type? = null
        }

    override fun adapt(call: Call<NetworkResponse<T>>): Call<Resource<T>> {
        return ResourceCall(call, successBodyType, json)
    }
}