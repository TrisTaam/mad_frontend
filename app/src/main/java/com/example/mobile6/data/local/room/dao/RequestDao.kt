package com.example.mobile6.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobile6.data.local.room.entity.RequestEntity
import java.util.Date

@Dao
interface RequestDao {
    @Query("SELECT * FROM requests WHERE request = :request LIMIT 1")
    suspend fun getCachedResponse(request: String): RequestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveResponse(requestEntity: RequestEntity)

    @Query("DELETE FROM requests WHERE cached_at < :date")
    suspend fun deleteOldCache(date: Date)

    @Query("DELETE FROM requests")
    suspend fun clearCache()
}