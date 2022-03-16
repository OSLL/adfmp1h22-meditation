package ru.hse.meditation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AboutActivity : ActionWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }
}
