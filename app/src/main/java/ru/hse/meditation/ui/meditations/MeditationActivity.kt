package ru.hse.meditation.ui.meditations

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import ru.hse.meditation.databinding.ActivityMeditationBinding
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.model.repository.MusicRepository
import ru.hse.meditation.ui.ActivityWithBackButton

class MeditationActivity : ActivityWithBackButton() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMeditationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val practice = intent.getSerializableExtra("practice") as Practice
        title = practice.name

        val timer = object : CountDownTimer(practice.duration * 60 * 1000L, 1000) {
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

        val musicRepository = MusicRepository(application)
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(musicRepository.getMusicFileFor(practice.courseId, practice.audioName).absolutePath)
        mediaPlayer.isLooping = true
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
    }
}
