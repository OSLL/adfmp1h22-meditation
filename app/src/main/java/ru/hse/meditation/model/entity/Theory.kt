package ru.hse.meditation.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["course_id", "level", "order"],
    foreignKeys = [ForeignKey(
        entity = Course::class,
        parentColumns = ["id"],
        childColumns = ["course_id"]
    )]
)
data class Theory(
    @ColumnInfo(name = "course_id")
    val courseId: String,
    val name: String,
    val text: String,
    val level: Int,
    val order: Int
)
