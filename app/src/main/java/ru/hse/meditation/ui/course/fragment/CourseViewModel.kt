package ru.hse.meditation.ui.course.fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    lateinit var currentCourse: Course

    val currentCourseLiveData: LiveData<Course> = courseRepository.getActive()

    fun changeLevel(newLevel: Int) {
        currentCourse.currentLevel = newLevel
        viewModelScope.launch(Dispatchers.IO) {
            courseRepository.update(currentCourse)
        }
    }

    suspend fun currentTheory(course: Course): List<Theory> = withContext(Dispatchers.IO) {
        theoryRepository.getLevelTheory(course.id, course.currentLevel)
    }

    suspend fun currentPractice(course: Course): List<Practice> = withContext(Dispatchers.IO) {
        practiceRepository.getLevelPractice(course.id, course.currentLevel)
    }
}
