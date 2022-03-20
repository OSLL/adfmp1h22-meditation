package ru.hse.meditation.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["course_id", "level", "order"],
    foreignKeys = [ForeignKey(
        entity = Course::class,
        parentColumns = ["id"],
        childColumns = ["course_id"]
    )],
    indices = [Index("course_id", "level")]
)
data class Theory(
    @ColumnInfo(name = "course_id")
    val courseId: String,
    val name: String,
    val text: String,
    val level: Int,
    val order: Int
)
