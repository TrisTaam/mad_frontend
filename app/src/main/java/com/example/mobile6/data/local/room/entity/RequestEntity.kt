package com.example.mobile6.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "requests")
data class RequestEntity(
    @PrimaryKey()
    @ColumnInfo(name = "request")
    val request: String = "",

    @ColumnInfo(name = "response")
    val response: String,

    @ColumnInfo(name = "cached_at")
    val cachedAt: Date
)