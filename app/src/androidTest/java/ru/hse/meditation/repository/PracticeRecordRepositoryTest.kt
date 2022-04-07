package ru.hse.meditation.repository

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import ru.hse.meditation.awaitValue
import ru.hse.meditation.model.entity.PracticeRecord
import ru.hse.meditation.model.repository.PracticeRecordRepository
import java.util.*

@RunWith(AndroidJUnit4::class)
class PracticeRecordRepositoryTest {
    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val practiceRecordRepository =
        PracticeRecordRepository(appContext.applicationContext as Application)

    private val practiceRecord1 = PracticeRecord("courseId", "practiceName1", Date(), 1, "comment1", 4)
    private val practiceRecord2 = PracticeRecord("courseId", "practiceName2", Date(), 2, "comment2", 4)

    @Test
    fun testAddPracticeRecord() {
        runBlocking {
            practiceRecordRepository.insert(practiceRecord1)

            val practiceRecords = practiceRecordRepository.getAll().awaitValue()!!

            assertEquals(4, practiceRecords.size)
            assertTrue(practiceRecords.contains(practiceRecord1))
        }
    }

    @Test
    fun testAddAndUpdatePracticeRecord() {
        runBlocking {
            practiceRecordRepository.insert(practiceRecord1)

            var practiceRecords = practiceRecordRepository.getAll().awaitValue()!!
            assertTrue(practiceRecords.contains(practiceRecord1))

            practiceRecordRepository.update(practiceRecord2)
            practiceRecords = practiceRecordRepository.getAll().awaitValue()!!

            assertEquals(4, practiceRecords.size)
            assertTrue(practiceRecords.contains(practiceRecord2))
            assertFalse(practiceRecords.contains(practiceRecord1))
        }
    }

    @Test
    fun testDeletePracticeRecord() {
        runBlocking {
            practiceRecordRepository.insert(practiceRecord1)

            var practiceRecords = practiceRecordRepository.getAll().awaitValue()!!
            assertTrue(practiceRecords.contains(practiceRecord1))

            practiceRecordRepository.delete(practiceRecord1)
            practiceRecords = practiceRecordRepository.getAll().awaitValue()!!

            assertEquals(3, practiceRecords.size)
            assertFalse(practiceRecords.contains(practiceRecord1))
        }
    }
}
