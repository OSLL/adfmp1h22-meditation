package ru.hse.meditation

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChangeCourseActivity : ActionWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_course)

        val listOfCourses: ListView = findViewById(R.id.list_of_courses)
        val adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_list_item_1,
            listOf("Course1", "Course2")
        )

        listOfCourses.adapter = adapter

        val fab: FloatingActionButton = findViewById(R.id.floating_action_button)
        fab.setOnClickListener {
            val intent = Intent(applicationContext, AddCourseActivity::class.java)
            startActivity(intent)
        }
    }
}