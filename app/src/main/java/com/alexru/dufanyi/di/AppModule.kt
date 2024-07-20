package com.alexru.dufanyi.di

import android.content.Context
import androidx.compose.ui.text.rememberTextMeasurer
import com.alexru.dufanyi.util.ResourceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideResourceManager(@ApplicationContext context: Context): ResourceManager {
        return ResourceManager(
            context = context,
        )
    }

}