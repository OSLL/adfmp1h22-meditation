package ru.hse.meditation.ui.course.change

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.hse.meditation.R
import ru.hse.meditation.model.repository.CourseRepository
import ru.hse.meditation.ui.ActivityWithBackButton
import ru.hse.meditation.ui.adapter.ChangeCourseAdapter
import ru.hse.meditation.ui.course.add.AddCourseActivity

class ChangeCourseActivity : ActivityWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_course)

        val listOfCourses: RecyclerView = findViewById(R.id.list_of_courses)
        listOfCourses.layoutManager = LinearLayoutManager(applicationContext)

        val adapter = ChangeCourseAdapter(this)
        listOfCourses.adapter = adapter

        val courseRepository = CourseRepository(application)

        val courses = courseRepository.getAll()
        courses.observe(this) {
            adapter.setCourseList(it)
        }

        val fab: FloatingActionButton = findViewById(R.id.floating_action_button)
        fab.setOnClickListener {
            val intent = Intent(applicationContext, AddCourseActivity::class.java)
            startActivity(intent)
        }
    }
}
