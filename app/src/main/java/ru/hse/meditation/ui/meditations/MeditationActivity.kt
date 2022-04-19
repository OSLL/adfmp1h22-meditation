package ru.hse.meditation.ui.meditations

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.hse.meditation.R
import ru.hse.meditation.databinding.ActivityMeditationBinding
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.ui.ActivityWithBackButton
import ru.hse.meditation.ui.notifications.MediaSessionService
import ru.hse.meditation.ui.notifications.audioIntentForService

const val audioIntent = "AudioIntent"

class MeditationActivity : ActivityWithBackButton() {
    lateinit var binding: ActivityMeditationBinding

    override fun onResume() {
        super.onResume()

        val playButton: Button = findViewById(R.id.play)
        val pauseButton: Button = findViewById(R.id.pause)
        val stopButton: Button = findViewById(R.id.stop)

        val broadcastManager = LocalBroadcastManager.getInstance(this)
        playButton.setOnClickListener {
            broadcastManager.sendBroadcast(Intent(audioIntentForService).also {
                it.putExtra("switch", "play")
            })
        }
        pauseButton.setOnClickListener {
            broadcastManager.sendBroadcast(Intent(audioIntentForService).also {
                Log.e("switch Activity", "pause")
                it.putExtra("switch", "pause")
            })
        }

        stopButton.setOnClickListener {
            broadcastManager.sendBroadcast(Intent(audioIntentForService).also {
                it.putExtra("switch", "stop")
            })
            onBackPressed()
        }

        broadcastManager.registerReceiver(messageReceiver, IntentFilter(audioIntent))
    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == audioIntent) {
                val playButton: Button = findViewById(R.id.play)
                val pauseButton: Button = findViewById(R.id.pause)

                intent.getStringExtra("time")?.let { time ->
                    binding.timer.text = time
                }
                intent.getStringExtra("switch")?.let { switch ->
                    when (switch) {
                        "play" -> {
                            playButton.visibility = View.GONE
                            pauseButton.visibility = View.VISIBLE
                        }
                        else -> {
                            playButton.visibility = View.VISIBLE
                            pauseButton.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeditationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val practice = intent.getSerializableExtra("practice") as Practice
        title = practice.name

        val serviceOn = intent.getBooleanExtra("serviceOn", false)
        if (serviceOn) return

        val intent = Intent(applicationContext, MediaSessionService::class.java)
        intent.putExtra("practice", practice)

        ContextCompat.startForegroundService(
            applicationContext,
            intent
        )
    }

}
