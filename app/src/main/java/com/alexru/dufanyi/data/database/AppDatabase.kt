package com.alexru.dufanyi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexru.dufanyi.data.dao.ChaptersDao
import com.alexru.dufanyi.data.dao.PagesDao
import com.alexru.dufanyi.data.dao.SeriesDao
import com.alexru.dufanyi.data.entity.SeriesEntity
import com.alexru.dufanyi.data.entity.ChapterEntity
import com.alexru.dufanyi.data.entity.PageEntity

@Database(
    entities = [
        SeriesEntity::class,
        ChapterEntity::class,
        PageEntity::class
   ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun seriesDao(): SeriesDao

    abstract fun chaptersDao(): ChaptersDao

    abstract fun pagesDao(): PagesDao

}