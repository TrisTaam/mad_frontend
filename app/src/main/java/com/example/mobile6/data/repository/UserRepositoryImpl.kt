package com.example.mobile6.data.repository

import com.example.mobile6.data.remote.service.TestService
import com.example.mobile6.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val testService: TestService
) : UserRepository {

}