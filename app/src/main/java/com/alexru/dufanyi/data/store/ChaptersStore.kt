package com.alexru.dufanyi.data.store

import com.alexru.dufanyi.data.dao.ChaptersDao
import com.alexru.dufanyi.data.entity.ChapterEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChaptersStore @Inject constructor(
    private val chaptersDao: ChaptersDao
) {

    suspend fun addChapters(chapters: Collection<ChapterEntity>, seriesId: Long) {
        chapters.forEach {
            val chapterId = chaptersDao.insert(it)
            updateChapterSeriesId(chapterId, seriesId)
        }
    }

    fun getAllChapters() = chaptersDao.getAllChapters()

    fun getChapters(seriesId: Long) = chaptersDao.getChapters(seriesId)

    fun getChapter(chapterId: Long) = chaptersDao.getChapter(chapterId)

    suspend fun updateChapterRead(chapterId: Long, page: Int) {
        chaptersDao.updateChapterRead(chapterId, page)
    }

    suspend fun updateChapterRead(chapterId: Long) {
        chaptersDao.updateChapterRead(chapterId)
    }

    private suspend fun updateChapterSeriesId(chapterId: Long, seriesId: Long) {
        chaptersDao.updateChapterSeriesId(chapterId, seriesId)
    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: ChaptersStore? = null

        fun getInstance(chaptersDao: ChaptersDao) =
            instance ?: synchronized(this) {
                instance ?: ChaptersStore(chaptersDao).also { instance = it }
            }
    }
}