package ru.hse.meditation.ui.course.theory

import android.os.Bundle
import ru.hse.meditation.databinding.ActivityTheoryBinding
import ru.hse.meditation.model.entity.Theory
import ru.hse.meditation.ui.ActivityWithBackButton

class TheoryActivity : ActivityWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTheoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val theory = intent.getSerializableExtra("theory") as Theory
        title = theory.name
        binding.meditationDescription.text = theory.text
    }
}
