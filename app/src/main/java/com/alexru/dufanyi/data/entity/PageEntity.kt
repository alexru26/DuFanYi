package com.alexru.dufanyi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PageEntity(
    @PrimaryKey(autoGenerate = true) val pageId: Long = 0,
    val number: Long,
    val text: String,
    val seriesCreatorId: Long
)