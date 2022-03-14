package ru.hse.meditation

import android.os.Bundle

class CreateCourseActivity : ActionWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_course)
    }
}