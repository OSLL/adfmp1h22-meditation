package ru.hse.meditation.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.hse.meditation.model.entity.Course

@Dao
interface CourseDao {
    @Query("SELECT * FROM course ORDER BY name")
    fun getAll(): LiveData<List<Course>>

    @Query("SELECT * FROM course WHERE is_active = 1")
    fun getActive(): LiveData<Course>

    @Insert
    fun insert(course: Course)

    @Update
    fun update(course: Course)

    @Delete
    fun delete(course: Course)

    @Transaction
    fun setActive(course: Course) {
        getActive().value?.let { currentActive ->
            currentActive.isActive = false
            update(currentActive)
        }
        course.isActive = true
        update(course)
    }
}
