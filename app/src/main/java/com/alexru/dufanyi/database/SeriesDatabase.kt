package com.alexru.dufanyi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.alexru.dufanyi.database.dao.SeriesDao
import com.alexru.dufanyi.database.entity.Series
import com.alexru.dufanyi.database.entity.Chapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date

@Database(
    entities = [
        Series::class,
        Chapter::class
   ],
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