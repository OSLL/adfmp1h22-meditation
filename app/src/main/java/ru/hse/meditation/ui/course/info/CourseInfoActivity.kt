package ru.hse.meditation.ui.course.info

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hse.meditation.R
import ru.hse.meditation.model.entity.Course
import ru.hse.meditation.model.repository.CourseRepository
import ru.hse.meditation.ui.ActivityWithBackButton

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

                        // TODO save music
                    }
                }
                progressBar.visibility = View.INVISIBLE
                onBackPressed()
            }
        }
    }
}
