package com.alexru.dufanyi.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alexru.dufanyi.data.entity.PageEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PagesDao : BaseDao<PageEntity> {

    @Query("SELECT * FROM pageentity")
    abstract fun getAllPages(): Flow<List<PageEntity>>

    @Query("SELECT * FROM pageentity WHERE seriesCreatorId = :seriesId")
    abstract fun getPages(seriesId: Long): Flow<List<PageEntity>>

    @Query("DELETE FROM pageentity WHERE seriesCreatorId = :seriesId")
    abstract suspend fun deletePages(seriesId: Long)

}