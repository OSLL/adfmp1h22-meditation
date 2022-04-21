package ru.hse.meditation.ui.course.fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.hse.meditation.model.entity.Course
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.model.entity.Theory
import ru.hse.meditation.model.repository.CourseRepository
import ru.hse.meditation.model.repository.PracticeRepository
import ru.hse.meditation.model.repository.TheoryRepository

class CourseViewModel(application: Application) : ViewModel() {
    private val courseRepository = CourseRepository(application)
    private val practiceRepository = PracticeRepository(application)
    private val theoryRepository = TheoryRepository(application)

    private lateinit var currentCourse: Course

    suspend fun changeLevel(newLevel: Int) = withContext(Dispatchers.IO) {
        currentCourse.currentLevel = newLevel
        courseRepository.update(currentCourse)
    }

    suspend fun currentCourse(): Course = withContext(Dispatchers.IO) {
        currentCourse = courseRepository.getActive()
        currentCourse
    }

    suspend fun currentTheory(course: Course): List<Theory> = withContext(Dispatchers.IO) {
        theoryRepository.getLevelTheory(course.id, course.currentLevel)
    }

    suspend fun currentPractice(course: Course): List<Practice> = withContext(Dispatchers.IO) {
        practiceRepository.getLevelPractice(course.id, course.currentLevel)
    }
}
