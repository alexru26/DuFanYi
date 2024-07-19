package com.alexru.dufanyi.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SeriesWithChapters(
    @Embedded val series: SeriesEntity,
    @Relation(
        parentColumn = "seriesId",
        entityColumn = "seriesCreatorId"
    )
    val chapters: List<ChapterEntity>,
    @Relation(
        parentColumn = "seriesId",
        entityColumn = "seriesCreatorId"
    )
    val pages: List<PageEntity>
)