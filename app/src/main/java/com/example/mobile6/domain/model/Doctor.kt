package com.example.mobile6.domain.model

/**
 * Doctor model tương ứng với entity Doctor ở backend
 */
data class Doctor(
    val id: Long,
    val specialty: String,
    val name: String?, // Nếu backend trả về tên bác sĩ, thêm trường này
    val avatarUrl: String? = null // Nếu backend trả về ảnh bác sĩ
)

/**
 * DoctorInfo model tương ứng với entity DoctorInfo ở backend
 */
data class DoctorInfo(
    val id: Long,
    val doctorId: Long,
    val title: String?,
    val description: String?,
    val order: Int?
)
