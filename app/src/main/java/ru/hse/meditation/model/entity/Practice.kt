package ru.hse.meditation.model.entity

import androidx.room.*
import java.io.Serializable
import java.util.*

@Entity(
    tableName = "practice",
    foreignKeys = [ForeignKey(
        entity = Course::class,
        parentColumns = ["id"],
        childColumns = ["course_id"]
    )],
    indices = [
        Index("course_id", "level", "order", unique = true),
        Index("course_id", "level")
    ]
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
    var duration: Int,
    @ColumnInfo(name = "last_practice_date_time")
    var lastPracticeDateTime: Date,
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Serializable
