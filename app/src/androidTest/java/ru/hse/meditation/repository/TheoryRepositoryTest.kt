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
import ru.hse.meditation.model.entity.Theory
import ru.hse.meditation.model.repository.CourseRepository
import ru.hse.meditation.model.repository.TheoryRepository

@RunWith(AndroidJUnit4::class)
class TheoryRepositoryTest : TestCase() {

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val theoryRepository =
        TheoryRepository(appContext.applicationContext as Application)

    private val courseId = "id"
    private val level = 1
    private val theory1 = Theory(
        courseId,
        "practiceName1",
        "description1",
        level,
        1,
        4
    )

    private val courseRepository = CourseRepository(appContext.applicationContext as Application)
    private val course = Course(courseId, "name1", "description1", 1)

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
            theoryRepository.insert(listOf(theory1))

            val practiceRecords = theoryRepository.getLevelTheory(courseId, level)

            Assert.assertEquals(1, practiceRecords.size)
            Assert.assertTrue(practiceRecords.contains(theory1))
        }
    }

    @Test
    fun testDeleteCourseTheory() {
        runBlocking {
            theoryRepository.insert(listOf(theory1))

            var practiceRecords = theoryRepository.getLevelTheory(courseId, level)
            Assert.assertTrue(practiceRecords.contains(theory1))

            theoryRepository.deleteCourseTheory(courseId)
            practiceRecords = theoryRepository.getLevelTheory(courseId, level)

            Assert.assertEquals(0, practiceRecords.size)
            Assert.assertFalse(practiceRecords.contains(theory1))
        }
    }
}