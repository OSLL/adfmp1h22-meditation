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
    fun getLevelTheory(courseId: String, level: Int): List<Theory>

    @Insert
    fun insert(entries: List<Theory>)

    @Delete(entity = Theory::class)
    fun deleteCourseTheory(courseId: String)
}
