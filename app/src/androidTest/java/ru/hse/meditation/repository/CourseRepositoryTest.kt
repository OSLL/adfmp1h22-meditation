package ru.hse.meditation.repository

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import ru.hse.meditation.awaitValue
import ru.hse.meditation.model.entity.Course
import ru.hse.meditation.model.repository.CourseRepository

@RunWith(AndroidJUnit4::class)
class CourseRepositoryTest {
    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val courseRepository = CourseRepository(appContext.applicationContext as Application)

    private val course1 = Course("id", "name1", "description1", 1)
    private val course2 = Course("id", "name2", "description2", 2)

    @Test
    fun testOneDefaultCourse() {
        runBlocking {
            val courses = courseRepository.getAll().awaitValue()!!

            assertEquals(1, courses.size)
        }
    }

    @Test
    fun testAddCourse() {
        runBlocking {
            courseRepository.insert(course1)

            val courses = courseRepository.getAll().awaitValue()!!

            assertEquals(2, courses.size)
            assertTrue(courses.contains(course1))
        }
    }

    @Test
    fun testAddAndUpdateCourse() {
        runBlocking {
            courseRepository.insert(course1)

            var courses = courseRepository.getAll().awaitValue()!!
            assertTrue(courses.contains(course1))

            courseRepository.update(course2)
            courses = courseRepository.getAll().awaitValue()!!

            assertEquals(2, courses.size)
            assertTrue(courses.contains(course2))
            assertFalse(courses.contains(course1))
        }
    }

    @Test
    fun testSetAndGetActiveCourse() {
        runBlocking {
            courseRepository.insert(course1)
            courseRepository.setActive(course1)

            val activeCourse = courseRepository.getActive().awaitValue()

            assertEquals(course1, activeCourse)
        }
    }

    @Test
    fun testDeleteCourse() {
        runBlocking {
            courseRepository.insert(course1)
            assertEquals(2, courseRepository.getAll().awaitValue()!!.size)

            courseRepository.delete(course1)

            val courses = courseRepository.getAll().awaitValue()!!
            assertEquals(1, courses.size)
            assertFalse(courses.contains(course1))
        }
    }
}
