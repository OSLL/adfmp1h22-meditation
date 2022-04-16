package ru.hse.meditation.ui.course.info

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import org.kohsuke.github.GitHub
import ru.hse.meditation.R
import ru.hse.meditation.model.entity.Course
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.model.entity.Theory
import ru.hse.meditation.model.repository.CourseRepository
import ru.hse.meditation.model.repository.PracticeRepository
import ru.hse.meditation.model.repository.TheoryRepository
import ru.hse.meditation.ui.ActivityWithBackButton
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CourseInfoActivity : ActivityWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_info)

        val course = intent.getSerializableExtra("course") as Course

        val textView: TextView = findViewById(R.id.info_about_course)
        textView.text = "Description course \"${course.name}\": ${course.description}"

        val progressBar: ProgressBar = findViewById(R.id.progressBarMusicFromGithub)

        val downloadCourseButton: Button = findViewById(R.id.download_course)

        downloadCourseButton.setOnClickListener {
            lifecycleScope.launch {
                progressBar.visibility = View.VISIBLE

                withContext(Dispatchers.IO) {
                    withContext(NonCancellable) {

                        val courseRepository = CourseRepository(application)
                        courseRepository.insert(course)

                        val theoryRepository = TheoryRepository(application)
                        theoryRepository.insert(getTheories(course.id))

                        val practiceRepository = PracticeRepository(application)
                        practiceRepository.insert(getPractices(course.id))

                        // TODO save music
                    }
                }
                progressBar.visibility = View.INVISIBLE
                onBackPressed()
            }
        }
    }

    private suspend fun getLevels(courseId: String): JSONArray {
        return suspendCoroutine { continuation ->
            thread {
                val gitHub = GitHub.connectAnonymously()
                val repo = gitHub.getRepository("KaperD/HSEMeditations")
                val dir = repo.getDirectoryContent("courses")

                val jsonString = dir.find { courseDir ->
                    courseDir.name == courseId && courseDir.isDirectory
                }?.listDirectoryContent()
                    ?.find { it.name == "course.json" }
                    ?.read()
                    ?.reader()
                    ?.readText() ?: throw RuntimeException()

                val json = JSONObject(jsonString)
                val levels = json.getJSONArray("levels")
                continuation.resume(levels)
            }
        }
    }

    private fun getTheories(courseId: String): List<Theory> {
        var levels: JSONArray
        runBlocking {
            levels = getLevels(courseId)
        }

        val theories = mutableListOf<Theory>()
        for (levelIndex in 0 until levels.length()) {
            val level = levels.getJSONObject(levelIndex)
            val jsonTheories = level.getJSONArray("theory")

            for (order in 0 until jsonTheories.length()) {
                val theory = jsonTheories.getJSONObject(order)
                theories.add(
                    Theory(
                        courseId = courseId,
                        name = theory.getString("name"),
                        text = theory.getString("text"),
                        level = levelIndex + 1,
                        order = order + 1
                    )
                )
            }
        }
        return theories
    }

    private fun getPractices(courseId: String): List<Practice> {
        var levels: JSONArray
        runBlocking {
            levels = getLevels(courseId)
        }

        val practices = mutableListOf<Practice>()
        for (levelIndex in 0 until levels.length()) {
            val level = levels.getJSONObject(levelIndex)
            val jsonPractices = level.getJSONArray("practice")

            for (order in 0 until jsonPractices.length()) {
                val practice = jsonPractices.getJSONObject(order)
                practices.add(
                    Practice(
                        courseId = courseId,
                        name = practice.getString("name"),
                        description = practice.getString("description"),
                        audioName = practice.getString("audio_name"),
                        level = levelIndex + 1,
                        order = order + 1,
                        duration = practice.getInt("duration"),
                        lastPracticeDateTime = Date(0L),
                        isFavorite = false
                    )
                )
            }
        }
        return practices
    }
}
