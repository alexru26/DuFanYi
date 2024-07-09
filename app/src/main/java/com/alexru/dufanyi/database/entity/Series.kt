package com.alexru.dufanyi.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Series(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val author: String,
    val status: String
//    val cover: String,
//    val chapters: List<Int>
)