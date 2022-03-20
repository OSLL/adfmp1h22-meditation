package ru.hse.meditation.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.hse.meditation.model.entity.Theory

@Dao
interface TheoryDao {
    @Query("""
        SELECT * FROM theory
        WHERE course_id = :courseId AND level = :level
        ORDER BY `order` ASC
    """)
    suspend fun getLevelTheory(courseId: String, level: Int): List<Theory>

    @Insert
    suspend fun insert(entries: List<Theory>)

    @Query("DELETE FROM theory WHERE course_id = :courseId")
    suspend fun deleteCourseTheory(courseId: String)
}
