package com.alexru.dufanyi.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SeriesWithChapters(
    @Embedded val series: Series,
    @Relation(
        parentColumn = "seriesId",
        entityColumn = "seriesCreatorId"
    )
    val chapters: List<Chapter>,
    @Relation(
        parentColumn = "seriesId",
        entityColumn = "seriesCreatorId"
    )
    val pages: List<Page>
)