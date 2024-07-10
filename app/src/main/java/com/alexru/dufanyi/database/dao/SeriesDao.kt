package com.alexru.dufanyi.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.alexru.dufanyi.database.entity.Chapter
import com.alexru.dufanyi.database.entity.Series
import com.alexru.dufanyi.database.entity.SeriesWithChapters
import kotlinx.coroutines.flow.Flow

@Dao
interface SeriesDao {

    @Upsert
    suspend fun upsertSeries(series: Series)

    @Delete
    suspend fun deleteSeries(series: Series)

    @Upsert
    suspend fun upsertChapter(chapter: Chapter)

    @Delete
    suspend fun deleteChapter(chapter: Chapter)

    @Query("DELETE FROM series")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM series")
    fun getAllSeriesWithChapters(): Flow<List<SeriesWithChapters>>

}