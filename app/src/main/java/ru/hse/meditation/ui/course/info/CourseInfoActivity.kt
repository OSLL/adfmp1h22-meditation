package ru.hse.meditation.ui.course.info

import android.os.Bundle
import android.widget.TextView
import ru.hse.meditation.R
import ru.hse.meditation.ui.ActivityWithBackButton


class CourseInfoActivity : ActivityWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_info)

        val course = intent.getStringExtra("course")

        val textView: TextView = findViewById(R.id.info_about_course)

        textView.text = "$course is about ..."
    }
}
