package com.alexru.dufanyi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alexru.dufanyi.database.entity.SeriesWithChapters

@Composable
fun SeriesCard(
    onSeriesClick: (Long) -> Unit,
    series: SeriesWithChapters,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        onClick = { onSeriesClick(series.series.seriesId) },
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
                text = series.series.name,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(10.dp)
            )
        }
    }
}