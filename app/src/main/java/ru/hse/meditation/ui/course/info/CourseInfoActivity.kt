package ru.hse.meditation.ui.course.info

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hse.meditation.databinding.ActivityCourseInfoBinding
import ru.hse.meditation.model.database.MeditationDatabase
import ru.hse.meditation.model.entity.Course
import ru.hse.meditation.model.repository.CourseRepository
import ru.hse.meditation.model.repository.MusicRepository
import ru.hse.meditation.model.repository.PracticeRepository
import ru.hse.meditation.model.repository.TheoryRepository
import ru.hse.meditation.ui.ActivityWithBackButton

class CourseInfoActivity : ActivityWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCourseInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val course = intent.getSerializableExtra("course") as Course

        binding.infoAboutCourse.text = course.description

        binding.downloadCourseButton.setOnClickListener {
            lifecycleScope.launch {
                binding.courseInfoBody.visibility = View.INVISIBLE
                binding.progressBarMusicFromGithub.visibility = View.VISIBLE

                withContext(Dispatchers.IO) {
                    val database = MeditationDatabase(application)
                    database.withTransaction {
                        try {
                            val courseRepository = CourseRepository(application)
                            courseRepository.insert(course)

                            val theoryRepository = TheoryRepository(application)
                            theoryRepository.insert(theoryRepository.loadTheoriesForCourse(course.id))

                            val practiceRepository = PracticeRepository(application)
                            practiceRepository.insert(practiceRepository.loadPracticesForCourse(course.id))

                            val musicRepository = MusicRepository(application)
                            musicRepository.loadMusicForCourse(
                                course.id,
                                binding.progressBarMusicFromGithub
                            )
                        } catch (e: Exception) {
                            Log.w("MEDITATION", e)

                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(application.applicationContext, e.message, Toast.LENGTH_SHORT).show()
                            }
                            throw e
                        }
                    }
                }
                onBackPressed()
            }
        }
    }
}
