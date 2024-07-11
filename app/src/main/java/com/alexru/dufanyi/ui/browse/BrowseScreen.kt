package com.alexru.dufanyi.ui.browse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.TextField
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alexru.dufanyi.database.dao.SeriesDao
import com.alexru.dufanyi.database.entity.Chapter
import com.alexru.dufanyi.database.entity.Series
import kotlinx.coroutines.launch
import com.alexru.dufanyi.networking.NetClient
import com.alexru.dufanyi.ui.components.BrowseTopBar
import util.onError
import util.onSuccess

@Composable
fun BrowseScreen(
    seriesDao: SeriesDao,
    netClient: NetClient
) {
    Scaffold(
        topBar = {
            BrowseTopBar()
        },
    ) { innerPadding ->
        BrowseScreen(
            seriesDao = seriesDao,
            netClient = netClient,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

@Composable
fun BrowseScreen(
    seriesDao: SeriesDao,
    netClient: NetClient,
    modifier: Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxSize()
    ) {
        SeriesUploadDialog(
            seriesDao = seriesDao,
            netClient = netClient,
        )
    }
}

@Composable
fun SeriesUploadDialog(
    seriesDao: SeriesDao,
    netClient: NetClient
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
                    netClient.getData(text)
                        .onSuccess { response ->
                            val series = extractSeriesData(response)
                            seriesDao.upsertSeries(series)
                            val chapters = extractChaptersData(response, netClient, text, seriesDao, series)
                            chapters.forEach { seriesDao.upsertChapter(it) }
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

fun extractSeriesData(
    response: String
): Series {
    val startIndex = response.indexOf("<h1 class=\"article-title\">")+26
    var endIndex = startIndex
    var check = response.substring(endIndex, endIndex+5)
    while(check != "</h1>") {
        endIndex++
        check = response.substring(endIndex, endIndex+5)
    }
    val full = response.substring(startIndex, endIndex)

    val name = full.substring(0, full.indexOf("_"))
    val author = full.substring(full.indexOf("_")+1, full.indexOf("【"))
    val status = full.substring(full.indexOf("【")+1, full.length-1)

    return Series(
        name = name,
        author = author,
        status = status,
    )
}

suspend fun extractChaptersData(
    response: String,
    netClient: NetClient,
    url: String,
    seriesDao: SeriesDao,
    series: Series
): List<Chapter> {
    val chaptersLength: Int = (response.length-response.replace("<li class=\"mulu\">", "").length)/17
    println(chaptersLength)
    val list = mutableListOf<Chapter>()
    val id = seriesDao.getSeriesByName(series.name).seriesId
    for(i in 2..chaptersLength+1) {
        var text = ""
        netClient.getData(url.substring(0, url.length-5)+"_"+i+".html")
            .onSuccess {
                val startIndex = it.indexOf("<div class=\"book_con fix\" id=\"text\">")+36
                var endIndex = startIndex
                var check = it.substring(endIndex, endIndex+6)
                while(check != "</div>") {
                    endIndex++
                    check = it.substring(endIndex, endIndex+6)
                }
                text = it.substring(startIndex, endIndex)

                text = text.replace("<p>", "").replace("</p>", "")

                while(text.indexOf("<a href") != -1) {
                    val startOfA = text.indexOf("<a href")
                    val endOfA = text.indexOf("</a>")+4
                    val substring = text.substring(startOfA, endOfA)
                    println(substring)
                    val carrotIndex = substring.indexOf(">")
                    val actualText = substring.substring(carrotIndex+1, substring.length-4)
                    println(actualText)
                    text = text.replace(substring, actualText)
                }
            }
            .onError {
                println(it)
            }
        val chapter = Chapter(
            number = (i-1).toLong(),
            name = "Page " + (i-1),
            text = text,
            seriesCreatorId = id
        )
        list.add(chapter)
    }
    return list
}