package com.alexru.dufanyi.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Chapter(
    @PrimaryKey(autoGenerate = true) val chapterId: Long = 0,
    val number: Long,
    val name: String,
    val startPage: Int,
    val endPage: Int, // inclusive
    val seriesCreatorId: Long
)