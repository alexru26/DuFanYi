package com.alexru.dufanyi.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Page(
    @PrimaryKey(autoGenerate = true) val pageId: Long = 0,
    val number: Long,
    val text: String,
    val seriesCreatorId: Long
)