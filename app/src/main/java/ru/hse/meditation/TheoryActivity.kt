package ru.hse.meditation

import android.os.Bundle
import ru.hse.meditation.databinding.ActivityTheoryBinding

class TheoryActivity : ActionWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTheoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Theory"
        binding.meditationDescription.text = intent.getStringExtra("description")
    }
}
