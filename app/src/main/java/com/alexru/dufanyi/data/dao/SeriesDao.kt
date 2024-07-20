package com.alexru.dufanyi.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.alexru.dufanyi.data.entity.SeriesEntity
import com.alexru.dufanyi.data.entity.SeriesWithChapters
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SeriesDao : BaseDao<SeriesEntity> {

//    @Query("DELETE FROM seriesentity")
//    abstract fun deleteAll()

    @Query("SELECT * FROM seriesentity")
    abstract fun getAllSeries(): Flow<List<SeriesEntity>>

    @Query("SELECT * FROM seriesentity WHERE seriesId = :seriesId")
    abstract fun getSeries(seriesId: Long): Flow<SeriesEntity>

    @Query("SELECT * FROM seriesentity WHERE name = :name")
    abstract fun getSeries(name: String): Flow<SeriesEntity>

    @Query("DELETE FROM seriesentity WHERE seriesId = :seriesId")
    abstract suspend fun deleteSeries(seriesId: Long)

    @Query("SELECT * FROM seriesentity WHERE seriesId = :seriesId")
    abstract fun getSeriesWithChapter(seriesId: Long): Flow<SeriesWithChapters>

    @Transaction
    @Query("SELECT * FROM seriesentity")
    abstract fun getAllSeriesWithChapters(): Flow<List<SeriesWithChapters>>

    @Query("SELECT COUNT(*) FROM seriesentity")
    abstract suspend fun count(): Int

}