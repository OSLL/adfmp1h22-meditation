package ru.hse.meditation

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class AddCourseActivity : ActionWithBackButton() {
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
    }
}