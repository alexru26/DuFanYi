package com.alexru.dufanyi.ui.reader

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexru.dufanyi.database.entity.Chapter
import com.alexru.dufanyi.database.entity.SeriesWithChapters
import com.alexru.dufanyi.ui.components.ReaderTopBar
import kotlin.math.ceil

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReaderScreen(
    seriesList: List<SeriesWithChapters>,
    chapterId: Long? = 0,
    modifier: Modifier
) {
    val chaptersList = mutableListOf<Chapter>()
    seriesList.forEach { series ->
        series.chapters.forEach { chapter ->
            chaptersList.add(chapter)
        }
    }
    val chapter = remember(chapterId) { chaptersList.find { it.chapterId == chapterId } }
    if(chapter != null) {
        val pages = splitText(chapter.text)
        val state = rememberPagerState { pages.size }
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = state,
                pageSize = PageSize.Fill,
                modifier = Modifier
                    .fillMaxSize()
            ) { page ->
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    Text(
                        text = pages[page],
                        modifier = Modifier
                            .padding(
                                horizontal = 8.dp,
                                vertical = 16.dp
                            )
                    )
                }
            }
            Box(
                modifier = modifier
                    .offset(y = -(8).dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text("${state.currentPage+1}/${state.pageCount}")
            }
        }
    }
}

fun splitText(text: String): List<String> {
    val lines = text.split("\n")
    val maxLines = 20
    var page = StringBuilder()
    var lineCounter = 0
    val list = mutableListOf<String>()
    for(i in lines.indices) {
        val line = lines[i]

        lineCounter += ceil(line.length/19.5).toInt()
        if(lineCounter > maxLines) {
            list.add(page.toString())
            page = StringBuilder()
            lineCounter = 0
        }
        page.append(line)
        page.append("\n\n")
        lineCounter += 2
        if(i == lines.size-1) {
            list.add(page.toString())
        }
    }
    return list
}