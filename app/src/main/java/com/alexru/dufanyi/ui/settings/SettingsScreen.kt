package com.alexru.dufanyi.ui.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alexru.dufanyi.ui.components.SettingsTopBar

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