package com.alexru.dufanyi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.alexru.dufanyi.database.dao.SeriesDao
import com.alexru.dufanyi.database.entity.Series

@Database(
    entities = [Series::class],
    version = 1
)
abstract class SeriesDatabase: RoomDatabase() {

    abstract fun seriesDao(): SeriesDao

}

fun getSeriesDatabase(context: Context): SeriesDatabase {
    val dbFile = context.getDatabasePath("series.db")
    return Room.databaseBuilder<SeriesDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()

}