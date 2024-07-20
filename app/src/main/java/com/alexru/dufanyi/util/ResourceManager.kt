package com.alexru.dufanyi.util

import android.content.Context
import android.net.Uri
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.unit.Constraints
import com.alexru.dufanyi.data.entity.ChapterEntity
import com.alexru.dufanyi.data.entity.PageEntity
import com.alexru.dufanyi.data.entity.SeriesEntity
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class ResourceManager @Inject constructor(
    private val context: Context,
) {

    fun readTextFromUri(uri: Uri): String {
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
    ): SeriesEntity {
        val cs: CharSequence = content
        val lines = cs.lines()
        return SeriesEntity(
            name = lines[0],
            author = lines[1],
            status = lines[2]
        )
    }

    fun extractChaptersData(
        content: String,
        textMeasurer: TextMeasurer
    ): Pair<List<ChapterEntity>, List<PageEntity>> {
        val cs: CharSequence = content
        val lines = cs.lines().slice(3..<cs.lines().size)
        val text = lines.joinToString(separator = "\n")

        val regexSplitter = Regex("((?=第(\\d+)章\\s*(.+)?))")
        val regex = Regex("""第(\d+)章\s*(.+)?""")

        val splitted = text.split(regexSplitter)
        val chapterSplits = splitted.slice(1..<splitted.size)

        var index = 1

        val chaptersList = mutableListOf<ChapterEntity>()
        val pagesList = mutableListOf<PageEntity>()

        for(chapter in chapterSplits) {
            val matchResult = regex.matchEntire(chapter.lines()[0])
            if (matchResult != null) {
                val chapterNumber = matchResult.destructured.toList()[0]
                val startIndex = index
                splitText(chapter, textMeasurer).forEach { page ->
                    pagesList.add(
                        PageEntity(
                            number = index.toLong(),
                            text = page,
                            seriesCreatorId = -1
                        )
                    )
                    index++
                }
                val endIndex = index - 1
                index++
                chaptersList.add(
                    ChapterEntity(
                        number = chapterNumber.toLong(),
                        name = chapter.lines()[0],
                        startPage = startIndex,
                        endPage = endIndex,
                        seriesCreatorId = -1
                    )
                )
            }
        }

        return Pair(chaptersList, pagesList)
    }

    fun splitText(
        text: String,
        textMeasurer: TextMeasurer
    ): List<String> {
        val lines = text.split("\n")
        val width = context.resources.displayMetrics.widthPixels
        val maxLines = 18
        var page = StringBuilder()
        var lineCounter = 0
        val list = mutableListOf<String>()
        for(i in lines.indices) {
            val line = lines[i]

            val count = textMeasurer.measure(
                line,
                constraints = Constraints(maxWidth = width-200),
            ).lineCount

            lineCounter += count

            if(lineCounter > maxLines) {
                list.add(page.toString())
                page = StringBuilder()
                lineCounter = 0
            }
            page.append(line)
            page.append("\n\n")
            lineCounter += 1
            if(i == lines.size-1) {
                list.add(page.toString())
            }
        }
        return list
    }

}