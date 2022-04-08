package ru.hse.meditation.repository

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.hse.meditation.awaitValue
import ru.hse.meditation.model.entity.Course
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.model.repository.CourseRepository
import ru.hse.meditation.model.repository.PracticeRepository
import java.util.*

@RunWith(AndroidJUnit4::class)
class PracticeRepositoryTest : TestCase() {

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val practiceRepository =
        PracticeRepository(appContext.applicationContext as Application)

    private val courseId = "courseId"
    private val practice1 = Practice(
        courseId,
        "practiceName1",
        "description1",
        "audioName1",
        10,
        100,
        5,
        Date(),
        false,
        4
    )
    private val practice2 =
        Practice(
            courseId,
            "practiceName2",
            "description2",
            "audioName2",
            2,
            2,
            5,
            Date(),
            false,
            4
        )

    private val courseRepository = CourseRepository(appContext.applicationContext as Application)

    private val course = Course(courseId, "name1", "description1", 1)
//    private val course2 = Course("id", "name2", "description2", 2)

    @Before
    fun insertCourseBeforeTests() {
        runBlocking {
            courseRepository.insert(course)

            val courses = courseRepository.getAll().awaitValue()!!

            Assert.assertEquals(2, courses.size)
            Assert.assertTrue(courses.contains(course))
        }
    }

    @Test
    fun testInsert() {
        runBlocking {
            practiceRepository.insert(listOf(practice1))

            val practiceRecords = practiceRepository.getAll().awaitValue()!!

            Assert.assertEquals(4, practiceRecords.size)
            Assert.assertTrue(practiceRecords.contains(practice1))
        }
    }

    @Test
    fun testUpdate() {
        runBlocking {
            practiceRepository.insert(listOf(practice1))

            var practiceRecords = practiceRepository.getAll().awaitValue()!!
            Assert.assertTrue(practiceRecords.contains(practice1))

            practiceRepository.update(practice2)
            practiceRecords = practiceRepository.getAll().awaitValue()!!

            Assert.assertEquals(4, practiceRecords.size)
            Assert.assertTrue(practiceRecords.contains(practice2))
            Assert.assertFalse(practiceRecords.contains(practice1))
        }
    }

    @Test
    fun testDeleteCoursePractice() {
        runBlocking {
            practiceRepository.insert(listOf(practice1))

            var practiceRecords = practiceRepository.getAll().awaitValue()!!
            Assert.assertTrue(practiceRecords.contains(practice1))

            practiceRepository.deleteCoursePractice(courseId)
            practiceRecords = practiceRepository.getAll().awaitValue()!!

            Assert.assertEquals(3, practiceRecords.size)
            Assert.assertFalse(practiceRecords.contains(practice1))
        }
    }
}