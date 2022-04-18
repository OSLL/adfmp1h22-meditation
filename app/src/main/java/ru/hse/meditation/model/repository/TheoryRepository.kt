package ru.hse.meditation.model.repository

import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import ru.hse.meditation.model.dao.TheoryDao
import ru.hse.meditation.model.database.MeditationDatabase
import ru.hse.meditation.model.entity.Theory

class TheoryRepository(application: Application) {
    private val theoryDao: TheoryDao

    init {
        val db = MeditationDatabase(application)
        theoryDao = db.theoryDao()
    }

    suspend fun getLevelTheory(courseId: String, level: Int): List<Theory> =
        theoryDao.getLevelTheory(courseId, level)

    suspend fun insert(entries: List<Theory>) = theoryDao.insert(entries)

    suspend fun deleteCourseTheory(courseId: String) = theoryDao.deleteCourseTheory(courseId)

    suspend fun loadTheoriesForCourse(courseId: String): List<Theory> {
        val root = Firebase.storage.reference
        val fileStream = root.child("courses/$courseId/course.json").stream.await().stream
        val jsonString = fileStream.reader().readText()
        val json = JSONObject(jsonString)
        val levels = json.getJSONArray("levels")
        val theories = mutableListOf<Theory>()
        for (levelIndex in 0 until levels.length()) {
            val level = levels.getJSONObject(levelIndex)
            val jsonTheories = level.getJSONArray("theory")

            for (order in 0 until jsonTheories.length()) {
                val theory = jsonTheories.getJSONObject(order)
                theories.add(
                    Theory(
                        courseId = courseId,
                        name = theory.getString("name"),
                        text = theory.getString("text"),
                        level = levelIndex + 1,
                        order = order + 1
                    )
                )
            }
        }
        return theories
    }
}
