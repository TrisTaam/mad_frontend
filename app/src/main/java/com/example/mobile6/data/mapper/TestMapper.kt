package com.example.mobile6.data.mapper

import com.example.mobile6.data.remote.dto.response.TestResponse
import com.example.mobile6.domain.model.Test

fun TestResponse.toTest(): Test {
    return Test(
        id = this.id,
        username = this.username
    )
}