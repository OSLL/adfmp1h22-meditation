package ru.hse.meditation.ui.course.add

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import ru.hse.meditation.R
import ru.hse.meditation.model.repository.CourseRepository
import ru.hse.meditation.ui.ActivityWithBackButton
import ru.hse.meditation.ui.adapter.AddCourseAdapter
import ru.hse.meditation.ui.course.create.CreateCourseActivity

class AddCourseActivity : ActivityWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        val listOfCourses: RecyclerView = findViewById(R.id.list_of_new_courses)
        listOfCourses.layoutManager = LinearLayoutManager(applicationContext)

        val adapter = AddCourseAdapter(this)
        listOfCourses.adapter = adapter

        val newCourseButton: Button = findViewById(R.id.new_course)

        val progressBar: ProgressBar = findViewById(R.id.progressBarCoursesFromGithub)

        lifecycleScope.launch {
            newCourseButton.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE

            withContext(Dispatchers.IO) {
                val courseRepository = CourseRepository(application)

                val courses = courseRepository.loadNewCourses()
                adapter.setCourseList(courses)
            }
            progressBar.visibility = View.INVISIBLE
            newCourseButton.visibility = View.VISIBLE
        }

        newCourseButton.setOnClickListener {
            val intent = Intent(applicationContext, CreateCourseActivity::class.java)
            startActivity(intent)
        }
    }
}
