package ru.hse.meditation.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import ru.hse.meditation.model.dao.PracticeDao
import ru.hse.meditation.model.database.MeditationDatabase
import ru.hse.meditation.model.entity.Practice
import java.util.*

class PracticeRepository(application: Application) {
    private val practiceDao: PracticeDao

    init {
        val db = MeditationDatabase(application)
        practiceDao = db.practiceDao()
    }

    fun getAll(): LiveData<List<Practice>> = practiceDao.getAll()

    fun getRecent(): LiveData<List<Practice>> = practiceDao.getRecent()

    fun getFavorite(): LiveData<List<Practice>> = practiceDao.getFavorite()

    suspend fun getLevelPractice(courseId: String, level: Int): List<Practice> =
        practiceDao.getLevelPractice(courseId, level)

    suspend fun getById(id: Int): Practice? = practiceDao.getById(id)

    suspend fun getRandom(): Practice = practiceDao.getRandom()

    suspend fun insert(entries: List<Practice>) = practiceDao.insert(entries)

    suspend fun update(practice: Practice) = practiceDao.update(practice)

    suspend fun deleteCoursePractice(courseId: String) = practiceDao.deleteCoursePractice(courseId)

    suspend fun loadPracticesForCourse(courseId: String): List<Practice> {
        val root = Firebase.storage.reference
        val fileStream = root.child("courses/$courseId/course.json").stream.await().stream
        val jsonString = fileStream.reader().readText()
        val json = JSONObject(jsonString)
        val levels = json.getJSONArray("levels")
        val practices = mutableListOf<Practice>()
        for (levelIndex in 0 until levels.length()) {
            val level = levels.getJSONObject(levelIndex)
            val jsonPractices = level.getJSONArray("practice")

            for (order in 0 until jsonPractices.length()) {
                val practice = jsonPractices.getJSONObject(order)
                practices.add(
                    Practice(
                        courseId = courseId,
                        name = practice.getString("name"),
                        description = practice.getString("description"),
                        audioName = practice.getString("audio_name"),
                        level = levelIndex + 1,
                        order = order + 1,
                        duration = practice.getInt("duration"),
                        lastPracticeDateTime = Date(0L),
                        isFavorite = false
                    )
                )
            }
        }
        return practices
    }
}
