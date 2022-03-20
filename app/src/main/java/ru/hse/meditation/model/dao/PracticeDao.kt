package ru.hse.meditation.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.hse.meditation.model.entity.Practice

@Dao
interface PracticeDao {
    @Query("SELECT * FROM practice ORDER BY name")
    fun getAll(): LiveData<List<Practice>>

    @Query("SELECT * FROM practice ORDER BY last_practice_date_time DESC LIMIT 10")
    fun getRecent(): LiveData<List<Practice>>

    @Query("SELECT * FROM practice WHERE is_favorite = 1 ORDER BY name")
    fun getFavorite(): LiveData<List<Practice>>

    @Query("""
        SELECT * FROM practice
        WHERE course_id = :courseId AND level = :level
        ORDER BY `order` ASC
    """)
    suspend fun getLevelPractice(courseId: String, level: Int): List<Practice>

    @Insert
    suspend fun insert(entries: List<Practice>)

    @Update
    suspend fun update(practice: Practice)

    @Query("DELETE FROM practice WHERE course_id = :courseId")
    suspend fun deleteCoursePractice(courseId: String)
}
