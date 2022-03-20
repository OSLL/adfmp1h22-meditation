package ru.hse.meditation.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import ru.hse.meditation.model.dao.PracticeDao
import ru.hse.meditation.model.database.MeditationDatabase
import ru.hse.meditation.model.entity.Practice

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

    suspend fun insert(entries: List<Practice>) = practiceDao.insert(entries)

    suspend fun update(practice: Practice) = practiceDao.update(practice)

    suspend fun deleteCoursePractice(courseId: String) = practiceDao.deleteCoursePractice(courseId)
}
