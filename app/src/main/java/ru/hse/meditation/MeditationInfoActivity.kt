package ru.hse.meditation

import android.os.Bundle
import ru.hse.meditation.databinding.ActivityMeditationInfoBinding

class MeditationInfoActivity : ActionWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMeditationInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Forest meditation"
        binding.meditationDescription.text = intent.getStringExtra("description")
    }
}
