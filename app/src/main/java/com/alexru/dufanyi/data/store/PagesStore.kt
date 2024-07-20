package com.alexru.dufanyi.data.store

import com.alexru.dufanyi.data.dao.PagesDao
import com.alexru.dufanyi.data.entity.PageEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PagesStore @Inject constructor(
    private val pagesDao: PagesDao
) {

    suspend fun addPages(pages: Collection<PageEntity>, seriesId: Long) {
        pages.forEach {
            val pageId = pagesDao.insert(it)
            updatePageSeriesId(pageId, seriesId)
        }
    }

    fun getAllChapters() = pagesDao.getAllPages()

    fun getChapters(seriesId: Long) = pagesDao.getPages(seriesId)

    private suspend fun updatePageSeriesId(pageId: Long, seriesId: Long) {
        pagesDao.updatePageSeriesId(pageId, seriesId)
    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: PagesStore? = null

        fun getInstance(pagesDao: PagesDao) =
            instance ?: synchronized(this) {
                instance ?: PagesStore(pagesDao).also { instance = it }
            }
    }
}