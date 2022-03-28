package ru.hse.meditation.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "course",
    indices = [Index("id")]
)
data class Course(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    @ColumnInfo(name = "number_of_levels")
    val numberOfLevels: Int,
    @ColumnInfo(name = "current_level")
    var currentLevel: Int = 1,
    @ColumnInfo(name = "is_active")
    var isActive: Boolean = false,
    @ColumnInfo(name = "can_be_deleted")
    val canBeDeleted: Boolean = true
)
