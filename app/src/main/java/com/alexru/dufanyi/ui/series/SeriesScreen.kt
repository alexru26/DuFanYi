package com.alexru.dufanyi.ui.series

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DownloadForOffline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexru.dufanyi.data.entity.ChapterEntity
import com.alexru.dufanyi.ui.components.SeriesTopBar

@Composable
fun SeriesScreen(
    onChapterClick: (Long) -> Unit,
    onNavigateBack: () -> Unit,
    seriesViewModel: SeriesViewModel = hiltViewModel()
) {
    val state by seriesViewModel.state.collectAsStateWithLifecycle()
    val onDeleteSeries = seriesViewModel::onDeleteSeries
    Scaffold(
        topBar = {
            SeriesTopBar(
                onNavigateBack = onNavigateBack,
                onDeleteSeries = {
                    onNavigateBack()
                    onDeleteSeries()
                }
            )
        },
    ) { innerPadding ->
        SeriesScreen(
            state = state,
            onChapterClick = onChapterClick,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

@Composable
fun SeriesScreen(
    state: SeriesUiState,
    onChapterClick: (Long) -> Unit,
    modifier: Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxSize()
    ) {
        if(state.name.isNotEmpty()) {
            SeriesScreenContent(
                state = state,
                onChapterClick = onChapterClick
            )
        }
    }
}

@Composable
fun SeriesScreenContent(
    state: SeriesUiState,
    onChapterClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        TitleCard(
            state = state
        )
        Spacer(Modifier.height(12.dp))
        ChapterListing(
            chapters = state.chaptersList,
            onChapterClick = onChapterClick
        )
    }
}

@Composable
fun TitleCard(
    state: SeriesUiState,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(
                horizontal = 16.dp,
                vertical = 32.dp
            )
    ) {
//        Image(
//            painter = painterResource(Res.drawable.l14),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(100.dp)
//                .aspectRatio(0.75F)
//                .clip(RoundedCornerShape(8.dp))
//        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .aspectRatio(0.75F)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray)
        )
        SeriesTitleCardText(
            name = state.name,
            author = state.author,
            status = state.status
        )
    }
}

@Composable
fun SeriesTitleCardText(
    name: String,
    author: String,
    status: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier
                .padding(bottom = 4.dp)
        )
        Text(
            text = author,
            style = MaterialTheme.typography.labelLarge,
            modifier = modifier
                .padding(bottom = 4.dp)
        )
        Text(
            text = status,
            style = MaterialTheme.typography.labelLarge,
            modifier = modifier
                .padding(bottom = 4.dp)
        )
    }
}

@Composable
fun ChapterListing(
    chapters: List<ChapterEntity>,
    onChapterClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        if(chapters.size == 1)
            Text(
                text = "1 chapter",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )
        else
            Text(
                text = "${chapters.size} chapters",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )
        chapters.sortedByDescending { it.number } .forEach { chapter ->
            Surface(
                onClick = { onChapterClick(chapter.chapterId) },
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 16.dp)
                    ) {
                        val opacity = if(!chapter.read) 1.0f else 0.5f
                        Text(chapter.name, color = MaterialTheme.colorScheme.onSurface.copy(alpha = opacity))
                        if(!chapter.read && chapter.currentPage != chapter.startPage) {
                            Text(
                                text = "Page: ${chapter.currentPage-chapter.startPage+1}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DownloadForOffline,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
