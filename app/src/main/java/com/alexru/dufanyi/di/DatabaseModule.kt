package com.alexru.dufanyi.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.alexru.dufanyi.data.dao.ChaptersDao
import com.alexru.dufanyi.data.dao.PagesDao
import com.alexru.dufanyi.data.dao.SeriesDao
import com.alexru.dufanyi.data.database.AppDatabase
import com.alexru.dufanyi.data.repository.SeriesRepository
import com.alexru.dufanyi.data.store.ChaptersStore
import com.alexru.dufanyi.data.store.PagesStore
import com.alexru.dufanyi.data.store.SeriesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{

    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        val dbFile = context.getDatabasePath("series.db")
        return Room.databaseBuilder<AppDatabase>(
            context = context,
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    @Provides
    fun provideSeriesDao(appDatabase: AppDatabase): SeriesDao {
        return appDatabase.seriesDao()
    }

    @Provides
    fun provideChapterDao(appDatabase: AppDatabase): ChaptersDao {
        return appDatabase.chaptersDao()
    }

    @Provides
    fun providePagesDao(appDatabase: AppDatabase): PagesDao {
        return appDatabase.pagesDao()
    }

    @Provides
    @Singleton
    fun providesSeriesStore(seriesDao: SeriesDao): SeriesStore {
        return SeriesStore(
            seriesDao = seriesDao
        )
    }

    @Provides
    @Singleton
    fun providesChaptersStore(chaptersDao: ChaptersDao): ChaptersStore {
        return ChaptersStore(
            chaptersDao = chaptersDao
        )
    }

    @Provides
    @Singleton
    fun providesPagesStore(pagesDao: PagesDao): PagesStore {
        return PagesStore(
            pagesDao = pagesDao
        )
    }

    @Provides
    @Singleton
    fun provideSeriesRepository(seriesStore: SeriesStore, chaptersStore: ChaptersStore, pagesStore: PagesStore, mainDispatcher: CoroutineDispatcher): SeriesRepository {
        return SeriesRepository(
            seriesStore = seriesStore,
            chaptersStore = chaptersStore,
            pagesStore = pagesStore,
            mainDispatcher = mainDispatcher
        )
    }

}