package ru.hse.meditation.ui.course.info

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import ru.hse.meditation.R
import ru.hse.meditation.databinding.ActivityCourseInfoBinding
import ru.hse.meditation.model.entity.Course
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.model.entity.Theory
import ru.hse.meditation.model.repository.CourseRepository
import ru.hse.meditation.model.repository.MusicRepository
import ru.hse.meditation.model.repository.PracticeRepository
import ru.hse.meditation.model.repository.TheoryRepository
import ru.hse.meditation.ui.ActivityWithBackButton
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CourseInfoActivity : ActivityWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCourseInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val course = intent.getSerializableExtra("course") as Course

        binding.infoAboutCourse.text = "Description course \"${course.name}\": ${course.description}"

        binding.downloadCourseButton.setOnClickListener {
            lifecycleScope.launch {
                binding.courseInfoBody.visibility = View.INVISIBLE
                binding.progressBarMusicFromGithub.visibility = View.VISIBLE


                withContext(Dispatchers.IO) {
                    withContext(NonCancellable) {

                        val courseRepository = CourseRepository(application)
                        courseRepository.insert(course)

                        val theoryRepository = TheoryRepository(application)
                        theoryRepository.insert(theoryRepository.loadTheoriesForCourse(course.id))

                        val practiceRepository = PracticeRepository(application)
                        practiceRepository.insert(practiceRepository.loadPracticesForCourse(course.id))

                        val musicRepository = MusicRepository(application)
                        musicRepository.loadMusicForCourse(course.id, binding.progressBarMusicFromGithub)
                    }
                }
                onBackPressed()
            }
        }
    }
}
