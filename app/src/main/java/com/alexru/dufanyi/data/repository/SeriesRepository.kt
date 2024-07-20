package com.alexru.dufanyi.data.repository

import com.alexru.dufanyi.data.dao.ChaptersDao
import com.alexru.dufanyi.data.dao.PagesDao
import com.alexru.dufanyi.data.dao.SeriesDao
import com.alexru.dufanyi.data.entity.ChapterEntity
import com.alexru.dufanyi.data.entity.PageEntity
import com.alexru.dufanyi.data.entity.SeriesEntity
import com.alexru.dufanyi.data.store.ChaptersStore
import com.alexru.dufanyi.data.store.PagesStore
import com.alexru.dufanyi.data.store.SeriesStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeriesRepository @Inject constructor(
    private val seriesStore: SeriesStore,
    private val chaptersStore: ChaptersStore,
    private val pagesStore: PagesStore,
    mainDispatcher: CoroutineDispatcher
) {
    private var refreshingJob: Job? = null

    private val scope = CoroutineScope(mainDispatcher)

    suspend fun updateSeries(series: SeriesEntity, chapters: List<ChapterEntity>, pages: List<PageEntity>) {
        if(refreshingJob?.isActive == true) {
            refreshingJob?.join()
        } else {
            refreshingJob = scope.launch {
                val seriesId = seriesStore.addSeries(series)
                chaptersStore.addChapters(chapters, seriesId)
                pagesStore.addPages(pages, seriesId)
            }
        }
    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: SeriesRepository? = null

        fun getInstance(seriesStore: SeriesStore, chaptersStore: ChaptersStore, pagesStore: PagesStore, mainDispatcher: CoroutineDispatcher) =
            instance ?: synchronized(this) {
                instance ?: SeriesRepository(seriesStore, chaptersStore, pagesStore, mainDispatcher).also { instance = it }
            }
    }
}