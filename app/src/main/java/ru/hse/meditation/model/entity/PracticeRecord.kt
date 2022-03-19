package ru.hse.meditation.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.*

@Entity(
    tableName = "practice_record",
    foreignKeys = [ForeignKey(
        entity = Practice::class,
        parentColumns = ["course_id", "level", "order"],
        childColumns = ["course_id", "level", "order"]
    )]
)
data class PracticeRecord(
    @ColumnInfo(name = "course_id")
    val courseId: String,
    val level: Int,
    val order: Int,
    @ColumnInfo(name = "date_time")
    val dateTime: Date,
    val duration: Int,
    var comment: String
)
