package com.alexru.dufanyi.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alexru.dufanyi.database.entity.Series
import com.alexru.dufanyi.ui.components.SeriesCard

@Composable
fun LibraryScreen(
    series: List<Series>,
    onSeriesClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if(series.isEmpty()) {
            isEmptyCard()
        }
        else {
            SeriesCollection(
                series = series,
                onSeriesClick = onSeriesClick,
                modifier = modifier
            )
        }
    }
}

@Composable
fun SeriesCollection(
    series: List<Series>,
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
        items(series) { item ->
            SeriesCard(
                onSeriesClick = onSeriesClick,
                series = item
            )
        }
    }
}

@Composable
fun isEmptyCard() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Upload a file in com.alexru.dufanyi.Browse")
    }
}