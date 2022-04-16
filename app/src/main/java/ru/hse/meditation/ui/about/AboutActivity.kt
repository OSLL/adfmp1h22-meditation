package ru.hse.meditation.ui.about

import android.os.Bundle
import ru.hse.meditation.R
import ru.hse.meditation.ui.ActivityWithBackButton

class AboutActivity : ActivityWithBackButton() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }
}
