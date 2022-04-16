package ru.hse.meditation.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import ru.hse.meditation.model.dao.CourseDao
import ru.hse.meditation.model.database.MeditationDatabase
import ru.hse.meditation.model.entity.Course

class CourseRepository(application: Application) {
    private val courseDao: CourseDao

    init {
        val db = MeditationDatabase(application)
        courseDao = db.courseDao()
    }

    fun getAll(): LiveData<List<Course>> = courseDao.getAll()

    fun getActive(): LiveData<Course> = courseDao.getActive()

    suspend fun insert(course: Course) = courseDao.insert(course)

    suspend fun update(course: Course) = courseDao.update(course)

    suspend fun delete(course: Course) = courseDao.delete(course)

    suspend fun setActive(course: Course) = courseDao.setActive(course)

    suspend fun loadNewCourses(): List<Course> {
//        TODO("Load from Firebase")
        return listOf(
            Course(
                "id1",
                "Lesha",
                "",
                1
            ),
            Course(
                "id2",
                "Lesha2",
                "",
                1
            )
        )
    }
}
