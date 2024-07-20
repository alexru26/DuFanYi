package com.alexru.dufanyi.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alexru.dufanyi.data.entity.ChapterEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ChaptersDao : BaseDao<ChapterEntity> {

    @Query("SELECT * FROM chapterentity")
    abstract fun getAllChapters(): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapterentity WHERE seriesCreatorId = :seriesId")
    abstract fun getChapters(seriesId: Long): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapterentity WHERE chapterId = :chapterId")
    abstract fun getChapter(chapterId: Long): Flow<ChapterEntity>

    @Query("UPDATE chapterentity SET currentPage = :page WHERE chapterId = :chapterId")
    abstract suspend fun updateChapterRead(chapterId: Long, page: Int)

    @Query("UPDATE chapterentity SET read = 1 WHERE chapterId = :chapterId")
    abstract suspend fun updateChapterRead(chapterId: Long)

    @Query("UPDATE chapterentity SET seriesCreatorId = :seriesId WHERE chapterId = :chapterId")
    abstract suspend fun updateChapterSeriesId(chapterId: Long, seriesId: Long)

    @Query("DELETE FROM chapterentity WHERE seriesCreatorId = :seriesId")
    abstract suspend fun deleteChapters(seriesId: Long)

}