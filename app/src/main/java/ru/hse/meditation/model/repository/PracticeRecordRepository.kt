package ru.hse.meditation.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import ru.hse.meditation.model.dao.PracticeRecordDao
import ru.hse.meditation.model.database.MeditationDatabase
import ru.hse.meditation.model.entity.PracticeRecord

class PracticeRecordRepository(application: Application) {
    private val practiceRecordDao: PracticeRecordDao

    init {
        val db = MeditationDatabase(application)
        practiceRecordDao = db.practiceRecordDao()
    }

    fun getAll(): LiveData<List<PracticeRecord>> = practiceRecordDao.getAll()

    suspend fun insert(practiceRecord: PracticeRecord) = practiceRecordDao.insert(practiceRecord)

    suspend fun update(practiceRecord: PracticeRecord) = practiceRecordDao.update(practiceRecord)

    suspend fun deleteCoursePracticeRecords(courseId: String) =
        practiceRecordDao.deleteCoursePracticeRecords(courseId)
}
