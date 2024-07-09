package com.alexru.dufanyi.ui.browse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alexru.dufanyi.database.entity.Series
import kotlinx.coroutines.launch
import com.alexru.dufanyi.networking.ShukuClient
import util.onError
import util.onSuccess

@Composable
fun BrowseScreen(
    shukuClient: ShukuClient,
    onUpload: (Series) -> Unit,
) {
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
    ) {
        seriesUploadDialog(
            shukuClient = shukuClient,
            onUpload = onUpload
        )
    }
}

@Composable
fun seriesUploadDialog(
    shukuClient: ShukuClient,
    onUpload: (Series) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            )
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Link") }
        )

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    shukuClient.getSeriesData(text)
                        .onSuccess {
                            val series = extractData(it)
                            onUpload(series)
                        }
                        .onError {
                            println(it)
                        }
                    isLoading = false
                    text = ""
                }
            },
            modifier = Modifier
        ) {
            if(isLoading) {
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

fun extractData(
    response: String
): Series {
    val start_index = response.indexOf("<h1 class=\"article-title\">")+26
    var end_index = start_index
    var check = response.substring(end_index, end_index+5)
    while(check != "</h1>") {
        end_index++
        check = response.substring(end_index, end_index+5)
    }
    val full = response.substring(start_index, end_index)

    val name = full.substring(0, full.indexOf("_"))
    val author = full.substring(full.indexOf("_")+1, full.indexOf("【"))
    val status = full.substring(full.indexOf("【")+1, full.length-1)

    return Series(
        name = name,
        author = author,
        status = status
    )
}