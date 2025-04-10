package com.example.mobile6.data.repository

import com.example.mobile6.data.mapper.toTest
import com.example.mobile6.data.remote.service.TestService
import com.example.mobile6.data.remote.util.map
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.model.Test
import com.example.mobile6.domain.repository.TestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val testService: TestService
) : TestRepository {
    override suspend fun test3(): Resource<Test> = withContext(Dispatchers.IO) {
        testService.test3().map {
            it.toTest()
        }
    }
}