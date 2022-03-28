package ru.hse.meditation.model.entity

import androidx.room.*
import java.io.Serializable
import java.util.*

@Entity(
    tableName = "practice_record",
)
data class PracticeRecord(
    @ColumnInfo(name = "course_id")
    val courseId: String,
    @ColumnInfo(name = "practice_name")
    val practiceName: String,
    @ColumnInfo(name = "date_time")
    val dateTime: Date,
    val duration: Int,
    var comment: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Serializable
