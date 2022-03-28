package ru.hse.meditation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class ActivityWithBackButton : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
