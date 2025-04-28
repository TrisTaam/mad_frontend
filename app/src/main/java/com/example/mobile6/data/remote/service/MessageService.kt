package com.example.mobile6.data.remote.service

import com.example.mobile6.domain.model.Doctor
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MessageService {
    /**
     * Lấy danh sách bác sĩ đã từng tư vấn với user
     * @param id id của user hiện tại
     * @param userId id của user (truyền giống id)
     */
    @GET("/api/v1/message/getAllDoctors/{id}")
    suspend fun getAllDoctorsForUser(
        @Path("id") id: Long,
        @Query("userId") userId: Long
    ): List<Doctor>
}