package com.alexru.dufanyi.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexru.dufanyi.data.entity.SeriesEntity
import com.alexru.dufanyi.data.entity.SeriesWithChapters
import com.alexru.dufanyi.ui.components.LibraryTopBar

@Composable
fun LibraryScreen(
    onSeriesClick: (Long) -> Unit,
    libraryViewModel: LibraryViewModel = hiltViewModel<LibraryViewModel>()
) {
    val state by libraryViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            LibraryTopBar()
        },
    ) { innerPadding ->
        LibraryScreen(
            seriesList = state.seriesList,
            onSeriesClick = onSeriesClick,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

@Composable
fun LibraryScreen(
    seriesList: List<SeriesEntity>,
    onSeriesClick: (Long) -> Unit,
    modifier: Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxSize()
    ) {
        if(seriesList.isNotEmpty()) {
            SeriesCollection(
                seriesList = seriesList,
                onSeriesClick = onSeriesClick,
            )
        }
    }
}

@Composable
fun SeriesCollection(
    seriesList: List<SeriesEntity>,
    onSeriesClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            top = 12.dp,
            start = 6.dp,
            end = 6.dp,
            bottom = 12.dp
        ),
        modifier = modifier
    ) {
        items(seriesList) { item ->
            SeriesCard(
                onSeriesClick = onSeriesClick,
                series = item
            )
        }
    }
}

@Composable
fun SeriesCard(
    onSeriesClick: (Long) -> Unit,
    series: SeriesEntity,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        onClick = { onSeriesClick(series.seriesId) },
        modifier = modifier
            .padding(2.dp)
    ) {
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = modifier
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(0.75F)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .drawWithCache {
                        val gradient = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = size.height / 3,
                            endY = size.height
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient, blendMode = BlendMode.Multiply)
                        }
                    }
                    .background(Color.Gray)
            )
            Text(
                text = series.name,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(10.dp)
            )
        }
    }
}
