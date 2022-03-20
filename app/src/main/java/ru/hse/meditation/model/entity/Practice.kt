package ru.hse.meditation.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.*

@Entity(
    primaryKeys = ["course_id", "level", "order"],
    foreignKeys = [ForeignKey(
        entity = Course::class,
        parentColumns = ["id"],
        childColumns = ["course_id"]
    )],
    indices = [Index("course_id", "level", "order")]
)
data class Practice(
    @ColumnInfo(name = "course_id")
    val courseId: String,
    val name: String,
    val description: String,
    @ColumnInfo(name = "audio_name")
    val audioName: String,
    val level: Int,
    val order: Int,
    val duration: Int,
    @ColumnInfo(name = "last_practice_date_time")
    var lastPracticeDateTime: Date,
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean
)
