package ru.hse.meditation

import android.content.Intent
import android.os.Bundle
import ru.hse.meditation.databinding.ActivityMeditationInfoBinding

class MeditationInfoActivity : ActionWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMeditationInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Forest meditation"

        binding.meditationDescription.text = intent.getStringExtra("description")
        binding.button2.setOnClickListener {
            val myIntent = Intent(this, MeditationActivity::class.java)
            startActivity(myIntent)
        }
    }
}
