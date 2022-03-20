package ru.hse.meditation.model.repository

import android.app.Application
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
}
