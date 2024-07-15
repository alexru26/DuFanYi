package com.alexru.dufanyi.ui.library

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexru.dufanyi.database.entity.SeriesWithChapters
import com.alexru.dufanyi.ui.components.LibraryTopBar
import com.alexru.dufanyi.ui.components.SeriesCard

@Composable
fun LibraryScreen(
    seriesList: List<SeriesWithChapters>,
    onSeriesClick: (Long) -> Unit,
    libraryViewModel: LibraryViewModel = viewModel()
) {
    val viewState by libraryViewModel.state.collectAsState()

    Scaffold(
        topBar = {
            LibraryTopBar()
        },
    ) { innerPadding ->
        LibraryScreen(
            seriesList = seriesList,
            onSeriesClick = onSeriesClick,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

@Composable
fun LibraryScreen(
    seriesList: List<SeriesWithChapters>,
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
    seriesList: List<SeriesWithChapters>,
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
