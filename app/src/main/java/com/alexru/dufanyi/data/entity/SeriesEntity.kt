package com.alexru.dufanyi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SeriesEntity(
    @PrimaryKey(autoGenerate = true) val seriesId: Long = 0,
    val name: String,
    val author: String,
    val status: String,
)