package ru.hse.meditation.ui.course.add

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import ru.hse.meditation.ui.course.info.CourseInfoActivity
import ru.hse.meditation.ui.course.create.CreateCourseActivity
import ru.hse.meditation.R
import ru.hse.meditation.ui.ActivityWithBackButton


class AddCourseActivity : ActivityWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        val listOfCourses: ListView = findViewById(R.id.list_of_new_courses)
        val adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_list_item_1,
            listOf("New course1", "New course2")
        )

        listOfCourses.adapter = adapter

        val newCourseButton: Button = findViewById(R.id.new_course)
        newCourseButton.setOnClickListener {
            val intent = Intent(applicationContext, CreateCourseActivity::class.java)
            startActivity(intent)
        }

        listOfCourses.isClickable = true
        listOfCourses.onItemClickListener = OnItemClickListener { arg0, arg1, position, arg3 ->
            val course = listOfCourses.getItemAtPosition(position)

            val intent = Intent(applicationContext, CourseInfoActivity::class.java)
            intent.putExtra("course", course.toString())
            startActivity(intent)
        }
    }
}
