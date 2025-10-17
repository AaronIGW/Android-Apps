package edu.wcupa.awilliams.booktracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val genre: String,
    val status: BookStatus,
    val progress: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val finishedAt: Long? = null
)
