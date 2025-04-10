package com.example.mobile6.domain.repository

import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.model.Test

interface TestRepository {
    suspend fun test3(): Resource<Test>
}