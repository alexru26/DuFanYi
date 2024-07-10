package com.alexru.dufanyi.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alexru.dufanyi.ui.components.SeriesTopBar
import com.alexru.dufanyi.ui.components.SettingsTopBar
import com.alexru.dufanyi.ui.series.SeriesScreen

@Composable
fun SettingsScreen(

) {
    Scaffold(
        topBar = {
            SettingsTopBar()
        },
    ) { innerPadding ->
        SettingsScreen(
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

@Composable
fun SettingsScreen(
    modifier: Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxSize()
    ) {
        Text("SETTINGS")
    }
}