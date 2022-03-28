package ru.hse.meditation.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.hse.meditation.model.entity.PracticeRecord

@Dao
interface PracticeRecordDao {
    @Query("SELECT * FROM practice_record ORDER BY date_time DESC")
    fun getAll(): LiveData<List<PracticeRecord>>

    @Insert
    suspend fun insert(practiceRecord: PracticeRecord)

    @Update
    suspend fun update(practiceRecord: PracticeRecord)

    @Delete
    suspend fun delete(practiceRecord: PracticeRecord)

    @Query("DELETE FROM practice_record WHERE course_id = :courseId")
    suspend fun deleteCoursePracticeRecords(courseId: String)
}
