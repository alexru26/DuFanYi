package com.alexru.dufanyi.ui.browse

import android.content.Context
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.alexru.dufanyi.database.dao.SeriesDao
import com.alexru.dufanyi.database.entity.Chapter
import com.alexru.dufanyi.database.entity.Series
import kotlinx.coroutines.launch
import com.alexru.dufanyi.ui.components.BrowseTopBar
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun BrowseScreen(
    seriesDao: SeriesDao,
) {
    Scaffold(
        topBar = {
            BrowseTopBar()
        },
    ) { innerPadding ->
        BrowseScreen(
            seriesDao = seriesDao,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

@Composable
fun BrowseScreen(
    seriesDao: SeriesDao,
    modifier: Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxSize()
    ) {
        SeriesUploadDialog(
            seriesDao = seriesDao,
        )
    }
}

@Composable
fun SeriesUploadDialog(
    seriesDao: SeriesDao,
) {
    val context = LocalContext.current
    val result = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        result.value = it
    }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(result.value) {
        result.value?.let { uri ->
            isLoading = true
            scope.launch {
                val content = readTextFromUri(
                    context = context,
                    uri = uri
                )
                val series = extractSeriesData(
                    content = content
                )
                seriesDao.upsertSeries(series)
                val chapters = extractChaptersData(
                    content = content,
                    seriesId = seriesDao.getSeriesByName(series.name).seriesId
                )
                chapters.forEach { seriesDao.upsertChapter(it) }
                isLoading = false
            }
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

fun readTextFromUri(context: Context, uri: Uri): String {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val stringBuilder = StringBuilder()
    var line: String?

    try {
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
            stringBuilder.append('\n')
        }
    } finally {
        reader.close()
        inputStream?.close()
    }

    return stringBuilder.toString()
}

fun extractSeriesData(
    content: String
): Series {
    val cs: CharSequence = content
    val lines = cs.lines()
    return Series(
        name = lines[0],
        author = lines[1],
        status = lines[2]
    )
}

fun extractChaptersData(
    content: String,
    seriesId: Long
): List<Chapter> {
    val cs: CharSequence = content
    val lines = cs.lines().slice(3..<cs.lines().size)

    val list = mutableListOf<Chapter>()

    var first = true
    var number: Long = 1
    var name = ""
    var text: StringBuilder = StringBuilder()

    val regex = Regex("""第(\d+)章\s*(.+)?""")

    for(i in lines.indices) {
        val line = lines[i]
        val matchResult = regex.matchEntire(line)

        if(matchResult != null) {
            val (chapterNumber, title) = matchResult.destructured

            if(!first) {
                list.add(Chapter(
                    number = number,
                    name = name,
                    text = text.toString(),
                    seriesCreatorId = seriesId
                ))
            }
            first = false

            number = chapterNumber.toLong()
            name = line

            text = StringBuilder()
            text.append(line)
            text.append('\n')
        }
        else {
            text.append(line)
            text.append("\n")

            if(i == lines.size-1) {
                list.add(Chapter(
                    number = number,
                    name = name,
                    text = text.toString(),
                    seriesCreatorId = seriesId
                ))
            }
        }
    }

    return list
}