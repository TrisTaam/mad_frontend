package com.example.mobile6.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mobile6.data.local.room.converter.DateConverter
import com.example.mobile6.data.local.room.dao.RequestDao
import com.example.mobile6.data.local.room.entity.RequestEntity

@Database(
    entities = [RequestEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class MainDatabase : RoomDatabase() {
    abstract val requestDao: RequestDao

    companion object {
        const val DATABASE_NAME = "nhom6.db"
    }
}