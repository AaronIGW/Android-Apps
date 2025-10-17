package edu.wcupa.awilliams.firstapp.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ideas")
data class IdeaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val tagsCsv: String,
    val status: String = "Draft",
    val plannedDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "shots",
    indices = [Index("ideaId")],
    foreignKeys = [
        ForeignKey(
            entity = IdeaEntity::class,
            parentColumns = ["id"],
            childColumns = ["ideaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ShotEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ideaId: Int,
    val text: String,
    val done: Boolean = false,
    val ord: Int = 0
)
