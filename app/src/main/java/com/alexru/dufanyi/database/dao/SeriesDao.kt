package com.alexru.dufanyi.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.alexru.dufanyi.database.entity.Series
import kotlinx.coroutines.flow.Flow

@Dao
interface SeriesDao {

    @Upsert
    suspend fun upsert(series: Series)

    @Delete
    suspend fun delete(series: Series)

    @Query("DELETE FROM series")
    suspend fun deleteAll()

    @Query("SELECT * FROM series")
    fun getAllSeries(): Flow<List<Series>>

}