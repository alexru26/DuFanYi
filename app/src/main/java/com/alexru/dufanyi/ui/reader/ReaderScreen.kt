package com.alexru.dufanyi.ui.reader

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexru.dufanyi.database.entity.Chapter
import com.alexru.dufanyi.database.entity.SeriesWithChapters
import com.alexru.dufanyi.ui.components.ReaderTopBar

@Composable
fun ReaderScreen(
    seriesList: List<SeriesWithChapters>,
    chapterId: Long? = 0,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            ReaderTopBar(
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        ReaderScreen(
            seriesList = seriesList,
            chapterId = chapterId,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

@Composable
fun ReaderScreen(
    seriesList: List<SeriesWithChapters>,
    chapterId: Long? = 0,
    modifier: Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxSize()
    ) {
        var chaptersList = mutableListOf<Chapter>()
        seriesList.forEach { series ->
            series.chapters.forEach { chapter ->
                chaptersList.add(chapter)
            }
        }
        val chapter = remember(chapterId) { chaptersList.find { it.chapterId == chapterId } }
        Column(
            modifier = Modifier
                .padding(
                    bottom = 12.dp
                )
                .verticalScroll(rememberScrollState())
        ) {
            Text("READER: $chapterId")
            if(chapter != null) {
                Text(chapter.text)
            }
        }
    }
}