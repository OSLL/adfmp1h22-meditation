package ru.hse.meditation.ui.course.create

import android.os.Bundle
import ru.hse.meditation.R
import ru.hse.meditation.ui.ActivityWithBackButton

class CreateCourseActivity : ActivityWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_course)
    }
}
