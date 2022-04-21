package ru.hse.meditation.ui.meditations

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
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
    private var lastTime = ""
    private var isStopped = false

    override fun onResume() {
        super.onResume()
        if (isStopped) {
            onBackPressed()
        }

        binding.timer.text = lastTime

        val playButton: ImageButton = findViewById(R.id.play)
        val pauseButton: ImageButton = findViewById(R.id.pause)
        val stopButton: ImageButton = findViewById(R.id.stop)

        val broadcastManager = LocalBroadcastManager.getInstance(this)
        playButton.setOnClickListener {
            broadcastManager.sendBroadcast(Intent(audioIntentForService).also {
                it.putExtra("switch", "play")
            })
        }
        pauseButton.setOnClickListener {
            broadcastManager.sendBroadcast(Intent(audioIntentForService).also {
                it.putExtra("switch", "pause")
            })
        }

        stopButton.setOnClickListener {
            broadcastManager.sendBroadcast(Intent(audioIntentForService).also {
                it.putExtra("switch", "stop")
            })
        }

        broadcastManager.registerReceiver(messageReceiver, IntentFilter(audioIntent))
    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == audioIntent) {
                val playButton: ImageButton = findViewById(R.id.play)
                val pauseButton: ImageButton = findViewById(R.id.pause)

                intent.getStringExtra("time")?.let { time ->
                    lastTime = time
                    binding.timer.text = time
                }
                intent.getStringExtra("switch")?.let { switch ->
                    when (switch) {
                        "play" -> {
                            playButton.visibility = View.GONE
                            pauseButton.visibility = View.VISIBLE
                        }
                        "pause" -> {
                            playButton.visibility = View.VISIBLE
                            pauseButton.visibility = View.GONE
                        }
                        "stop" -> {
                            isStopped = true
                            onBackPressed()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
        super.onDestroy()
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
