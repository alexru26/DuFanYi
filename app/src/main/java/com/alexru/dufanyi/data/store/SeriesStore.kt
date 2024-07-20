package com.alexru.dufanyi.data.store

import com.alexru.dufanyi.data.dao.SeriesDao
import com.alexru.dufanyi.data.entity.SeriesEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeriesStore @Inject constructor(
    private val seriesDao: SeriesDao
) {

    suspend fun addSeries(series: SeriesEntity): Long {
        return seriesDao.insert(series)
    }

    fun getAllSeries() = seriesDao.getAllSeries()

    fun getSeries(seriesId: Long) = seriesDao.getSeries(seriesId)

    fun getSeries(name: String) = seriesDao.getSeries(name)

    fun getSeriesWithChapter(seriesId: Long) = seriesDao.getSeriesWithChapter(seriesId)

    suspend fun isEmpty(): Boolean = seriesDao.count() == 0

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: SeriesStore? = null

        fun getInstance(seriesDao: SeriesDao) =
            instance ?: synchronized(this) {
                instance ?: SeriesStore(seriesDao).also { instance = it }
            }
    }
}