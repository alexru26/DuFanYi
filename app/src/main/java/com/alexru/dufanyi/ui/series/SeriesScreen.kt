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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DownloadForOffline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alexru.dufanyi.database.entity.Chapter
import com.alexru.dufanyi.database.entity.SeriesWithChapters
import com.alexru.dufanyi.ui.components.SeriesTopBar

@Composable
fun SeriesScreen(
    seriesList: List<SeriesWithChapters>,
    seriesId: Long? = 0,
    onChapterClick: (Long) -> Unit,
    onNavigateBack: () -> Unit,
    onDeleteSeries: () -> Unit
) {
    Scaffold(
        topBar = {
            SeriesTopBar(
                onNavigateBack = onNavigateBack,
                onDeleteSeries = onDeleteSeries
            )
        },
    ) { innerPadding ->
        SeriesScreen(
            seriesList = seriesList,
            seriesId = seriesId,
            onChapterClick = onChapterClick,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

@Composable
fun SeriesScreen(
    seriesList: List<SeriesWithChapters>,
    seriesId: Long? = 0,
    onChapterClick: (Long) -> Unit,
    modifier: Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxSize()
    ) {
        val series = remember(seriesId) { seriesList.find { it.series.seriesId == seriesId } }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            if (series != null) {
                titleCard(
                    series
                )
            }
            Spacer(Modifier.height(12.dp))
            if (series != null) {
                chapterListing(
                    chapters = series.chapters,
                    onChapterClick = onChapterClick
                )
            }
        }
    }
}

@Composable
fun titleCard(
    series: SeriesWithChapters,
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
        seriesTitleCardText(series)
    }
}

@Composable
fun seriesTitleCardText(
    series: SeriesWithChapters,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = series.series.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier
                .padding(bottom = 4.dp)
        )
        Text(
            text = series.series.author,
            style = MaterialTheme.typography.labelLarge,
            modifier = modifier
                .padding(bottom = 4.dp)
        )
        Text(
            text = series.series.status,
            style = MaterialTheme.typography.labelLarge,
            modifier = modifier
                .padding(bottom = 4.dp)
        )
    }
}

@Composable
fun chapterListing(
    chapters: List<Chapter>,
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
                            .padding(start = 16.dp)
                    ) {
                        Text(chapter.name, Modifier.padding(bottom = 12.dp))
//                        Text(chapter.date)
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
