package com.alexru.dufanyi.ui.browse

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexru.dufanyi.ui.components.BrowseTopBar

@Composable
fun BrowseScreen(
    browseViewModel: BrowseViewModel = hiltViewModel<BrowseViewModel>()
) {
    val state by browseViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            BrowseTopBar()
        },
    ) { innerPadding ->
        BrowseScreen(
            isUploading = state.isUploading,
            onUpload = browseViewModel::onUpload,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

@Composable
fun BrowseScreen(
    isUploading: Boolean,
    onUpload: (Uri, TextMeasurer) -> Unit,
    modifier: Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxSize()
    ) {
        SeriesUploadDialog(
            isUploading = isUploading,
            onUpload = onUpload
        )
    }
}

@Composable
fun SeriesUploadDialog(
    isUploading: Boolean,
    onUpload: (Uri, TextMeasurer) -> Unit,
) {
    val textMeasurer = rememberTextMeasurer()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        if (it != null) {
            onUpload(it, textMeasurer)
        }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            )
    ) {
        Button(
            onClick = {
                launcher.launch(arrayOf("text/plain"))
            },
            modifier = Modifier
        ) {
            if(isUploading) {
                CircularProgressIndicator(
                    strokeWidth = 1.dp,
                    color = Color.White,
                    modifier = Modifier
                        .size(15.dp)
                )
            }
            else {
                Text("Upload series")
            }
        }
    }
}