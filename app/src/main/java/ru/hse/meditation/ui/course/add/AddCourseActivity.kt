package ru.hse.meditation.ui.course.add

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.hse.meditation.R
import ru.hse.meditation.model.entity.Course
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

        adapter.setCourseList(
            listOf(
                Course(
                    "id1",
                    "Lesha",
                    "",
                    1
                ),
                Course(
                    "id2",
                    "Lesha2",
                    "",
                    1
                )
            )
        )

//        TODO("Load from github")

        val newCourseButton: Button = findViewById(R.id.new_course)
        newCourseButton.setOnClickListener {
            val intent = Intent(applicationContext, CreateCourseActivity::class.java)
            startActivity(intent)
        }
    }
}
