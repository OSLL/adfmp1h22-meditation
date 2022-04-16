package ru.hse.meditation.ui.course.info

import android.os.Bundle
import android.widget.TextView
import ru.hse.meditation.R
import ru.hse.meditation.model.entity.Course
import ru.hse.meditation.ui.ActivityWithBackButton

class CourseInfoActivity : ActivityWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_info)

        val course = intent.getSerializableExtra("course") as Course

        val textView: TextView = findViewById(R.id.info_about_course)

        textView.text = "${course.name} is about ..."
    }
}
