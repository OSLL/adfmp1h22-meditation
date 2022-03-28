package ru.hse.meditation.model.entity

import androidx.room.*
import java.io.Serializable

@Entity(
    tableName = "theory",
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
data class Theory(
    @ColumnInfo(name = "course_id")
    val courseId: String,
    val name: String,
    val text: String,
    val level: Int,
    val order: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Serializable
