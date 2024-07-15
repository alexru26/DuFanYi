package com.alexru.dufanyi.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Chapter(
    @PrimaryKey(autoGenerate = true) val chapterId: Long = 0,
    val number: Long,
    val name: String,
    val startPage: Int,
    val endPage: Int, // inclusive
    val currentPage: Int = startPage,
    val read: Boolean = false,
    val seriesCreatorId: Long
)