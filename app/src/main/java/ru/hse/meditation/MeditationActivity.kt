package ru.hse.meditation

import android.os.Bundle
import android.os.CountDownTimer
import ru.hse.meditation.databinding.ActivityMeditationBinding

class MeditationActivity : ActionWithBackButton() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMeditationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Forest meditation"

        val timer = object : CountDownTimer(5 * 60 * 1000, 1000) {
            override fun onTick(millis: Long) {
                val totalSeconds = millis / 1000
                val seconds = totalSeconds % 60
                val minutes = totalSeconds / 60

                val text = "%02d:%02d".format(minutes, seconds)
                binding.timer.text = text
            }

            override fun onFinish() {
                onBackPressed()
            }
        }
        timer.start()

    }
}
