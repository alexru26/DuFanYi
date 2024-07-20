package com.alexru.dufanyi.ui.reader

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexru.dufanyi.data.entity.ChapterEntity
import com.alexru.dufanyi.data.entity.PageEntity
import com.alexru.dufanyi.ui.components.ReaderTopBar

@Composable
fun ReaderScreen(
    onNavigateBack: () -> Unit,
    readerViewModel: ReaderViewModel = hiltViewModel<ReaderViewModel>()
) {
    val state = readerViewModel.state

    Scaffold(
        topBar = {
            ReaderTopBar(
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        ReaderScreen(
            state = state,
            onChapterRead = readerViewModel::onChapterRead,
            onChapterFinished = readerViewModel::onChapterFinished,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}


@Composable
fun ReaderScreen(
    state: ReaderUiState,
    onChapterRead: (Long, Int) -> Unit,
    onChapterFinished: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxSize()
    ) {
        if (state.chaptersList.isNotEmpty()) {
            ReaderScreenContent(
                chapterId = state.chapterId,
                chaptersList = state.chaptersList,
                pagesList = state.pagesList,
                onChapterRead = onChapterRead,
                onChapterFinished = onChapterFinished
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReaderScreenContent(
    chapterId: Long,
    chaptersList: List<ChapterEntity>,
    pagesList: List<PageEntity>,
    onChapterRead: (Long, Int) -> Unit,
    onChapterFinished: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val chaptersIndexList = chaptersList.map { it.chapterId }
    val chaptersStartEndList = chaptersList.map { (it.startPage..it.endPage) }

    var chapterIndex by remember(chapterId) { mutableIntStateOf(chaptersIndexList.indexOf(chapterId)) }
    val chapter = remember(chapterIndex) { chaptersList[chapterIndex] }

    val pages = (0..chaptersList[chaptersList.lastIndex].endPage+1).toList()

    var currentPage by remember { mutableIntStateOf(if(chapter.read) chapter.startPage else chapter.currentPage) }
    val state = rememberPagerState(initialPage = currentPage) { pages.size }


    HorizontalPager(
        state = state,
        pageSize = PageSize.Fill,
        modifier = Modifier
            .fillMaxSize()
    ) { page ->

        // LOGIC
        val currentRange = chaptersStartEndList.find { it.contains(state.currentPage) }
        if(currentRange != null) {
            currentPage = state.currentPage
            chapterIndex = chaptersStartEndList.indexOf(currentRange)
            onChapterRead(chapter.chapterId, currentPage-chapter.startPage+1)
        }
        else {
            if(state.currentPage > currentPage) {
                onChapterFinished(chapter.chapterId)
            }
        }

        // UI
        val temp = chaptersStartEndList.find { it.contains(page) }
        if(temp == null) {
            Surface(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if(page < currentPage) {
                    BackTransitionPage(
                        chaptersList = chaptersList,
                        chapterIndex = chapterIndex
                    )
                }
                else if(page > currentPage) {
                    ForwardTransitionPage(
                        chaptersList = chaptersList,
                        chapterIndex = chapterIndex
                    )
                }
            }
        }
        else {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                modifier = modifier
                    .height(650.dp)
                    .fillMaxWidth()
            ) {
                val text = pagesList.find { it.number == page.toLong() }?.text ?: ""
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = text,
                        modifier = Modifier
                            .padding(
                                horizontal = 8.dp,
                                vertical = 16.dp
                            )
                    )
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "${currentPage-chapter.startPage + 1}/${chapter.endPage-chapter.startPage+1}",
            modifier = Modifier
                //.offset(y = -(8).dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun BackTransitionPage(
    chaptersList: List<ChapterEntity>,
    chapterIndex: Int
) {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        if(chapterIndex == 0) {
            AlertBox(
                string = "previous chapter",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
        else {
            ChapterInfoTextBox(
                string = "Previous:",
                chapterTitle = chaptersList[chapterIndex-1].name,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        ChapterInfoTextBox(
            string = "Current:",
            chapterTitle = chaptersList[chapterIndex].name,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun ForwardTransitionPage(
    chaptersList: List<ChapterEntity>,
    chapterIndex: Int
) {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        ChapterInfoTextBox(
            string = "Current:",
            chapterTitle = chaptersList[chapterIndex].name,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))
        if(chapterIndex == chaptersList.lastIndex) {
            AlertBox(
                string = "next chapter",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
        else {
            ChapterInfoTextBox(
                string = "Next:",
                chapterTitle = chaptersList[chapterIndex+1].name,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ChapterInfoTextBox(
    string: String,
    chapterTitle: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(225.dp)
    ) {
        Column {
            Text(
                text = string,
                color = Color.White,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
            Text(
                text = chapterTitle,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
            )
        }
    }

}

@Composable
fun AlertBox(
    string: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.Black,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .width(225.dp)
            .border(2.dp, Color.Gray, MaterialTheme.shapes.medium)
    ) {
        Row(
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                tint = Color.White,
                contentDescription = "info",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = "There's no $string",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}