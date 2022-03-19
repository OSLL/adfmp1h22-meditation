package ru.hse.meditation.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.hse.meditation.model.entity.PracticeRecord

@Dao
interface PracticeRecordDao {
    @Query("SELECT * FROM practice_record ORDER BY dateTime DESC")
    fun getAll(): LiveData<List<PracticeRecord>>

    @Insert
    fun insert(practiceRecord: PracticeRecord)

    @Update
    fun update(practiceRecord: PracticeRecord)

    @Delete(entity = PracticeRecord::class)
    fun deleteCoursePracticeRecords(courseId: String)
}
