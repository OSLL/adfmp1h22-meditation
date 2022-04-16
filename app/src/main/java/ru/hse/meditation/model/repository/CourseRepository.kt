package ru.hse.meditation.model.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
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

    suspend fun getAllAwait(): List<Course> = courseDao.getAllAwait()

    fun getActive(): LiveData<Course> = courseDao.getActive()

    suspend fun insert(course: Course) = courseDao.insert(course)

    suspend fun update(course: Course) = courseDao.update(course)

    suspend fun delete(course: Course) = courseDao.delete(course)

    suspend fun setActive(course: Course) = courseDao.setActive(course)

    suspend fun loadAllCourses(): List<Course> = withContext(Dispatchers.IO) {
        val root = Firebase.storage.reference
        Log.d("LESHA", "BEGIN")
        val coursesDirs = root.child("courses").listAll().await().prefixes
        Log.d("LESHA", "END")
        coursesDirs.map {
            Log.d("LESHA", "BEGIN1")
            val fileStream = it.child("course.json").stream.await().stream
            Log.d("LESHA", "END1")
            val jsonString = fileStream.reader().readText()
            val json = JSONObject(jsonString)
            Course(
                id = json.getString("id"),
                name = json.getString("name"),
                description = json.getString("description"),
                numberOfLevels = json.getJSONArray("levels").length()
            )
        }
    }
}
