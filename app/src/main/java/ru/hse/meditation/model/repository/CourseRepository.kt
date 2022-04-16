package ru.hse.meditation.model.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import org.json.JSONObject
import org.kohsuke.github.GitHub
import ru.hse.meditation.model.dao.CourseDao
import ru.hse.meditation.model.database.MeditationDatabase
import ru.hse.meditation.model.entity.Course
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CourseRepository(application: Application) {
    private val courseDao: CourseDao

    init {
        val db = MeditationDatabase(application)
        courseDao = db.courseDao()
    }

    fun getAll(): LiveData<List<Course>> = courseDao.getAll()

    suspend fun getAllAwait(): List<Course> = courseDao.getAllAwait()

    fun getActive(): LiveData<Course> = courseDao.getActive()

    suspend fun insert(course: Course) = courseDao.insert(course)

    suspend fun update(course: Course) = courseDao.update(course)

    suspend fun delete(course: Course) = courseDao.delete(course)

    suspend fun setActive(course: Course) = courseDao.setActive(course)

    suspend fun loadAllCourses(): List<Course> {
        return suspendCoroutine { continuation ->
            thread {
                try {
                    val gitHub = GitHub.connectAnonymously()
                    val repo = gitHub.getRepository("KaperD/HSEMeditations")
                    val dir = repo.getDirectoryContent("courses")

                    val allCourses = mutableListOf<Course>()
                    dir.forEach { courseDir ->
                        if (courseDir.isDirectory) {
                            val jsonString = courseDir.listDirectoryContent()
                                .find { it.name == "course.json" }
                                ?.read()
                                ?.reader()
                                ?.readText() ?: return@forEach
                            val json = JSONObject(jsonString)

                            allCourses.add(
                                Course(
                                    id = json.getString("id"),
                                    name = json.getString("name"),
                                    description = json.getString("description"),
                                    numberOfLevels = json.getJSONArray("levels").length()
                                )
                            )
                        }
                    }
                    continuation.resume(allCourses)
                } catch (e: Exception) {
                    Log.e("LESHA", e.message.toString())
                }
            }
        }
    }
}
