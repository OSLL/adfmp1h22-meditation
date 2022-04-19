package ru.hse.meditation.ui.notifications

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.KeyEvent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media.session.MediaButtonReceiver
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.model.repository.MusicRepository
import ru.hse.meditation.ui.meditations.audioIntent

const val audioIntentForService = "audioIntentForService"

class MediaSessionService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var mMediaNotificationManager: MediaNotificationManager? = null
    private var mediaSession: MediaSessionCompat? = null
    private var lastAudioPath: String? = null
    private val handler: Handler = Handler(Looper.getMainLooper())

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == audioIntentForService) {
                intent.getStringExtra("switch")?.also {
                    when (it) {
                        "pause" -> {
                            MediaButtonReceiver.buildMediaButtonPendingIntent(
                                this@MediaSessionService,
                                PlaybackStateCompat.ACTION_PAUSE
                            ).send()
                        }
                        "play" -> {
                            MediaButtonReceiver.buildMediaButtonPendingIntent(
                                this@MediaSessionService,
                                PlaybackStateCompat.ACTION_PLAY
                            ).send()
                        }
                        "stop" -> {
                            mediaPlayer?.reset()
                            mMediaNotificationManager!!.notificationManager.cancelAll()
                            stopForeground(true)
                            stopSelf()
                        }
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        val broadcastManager = LocalBroadcastManager.getInstance(this)
        broadcastManager.registerReceiver(messageReceiver, IntentFilter(audioIntentForService))
    }

    private val metadata: MediaMetadataCompat
        get() {
            val builder = MediaMetadataCompat.Builder()
            builder.putLong(
                MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer!!.duration
                    .toLong()
            )
            return builder.build()
        }
    private val state: PlaybackStateCompat
        get() {
            val actions =
                if (mediaPlayer!!.isPlaying) PlaybackStateCompat.ACTION_PAUSE else PlaybackStateCompat.ACTION_PLAY
            val state =
                if (mediaPlayer!!.isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED
            val stateBuilder = PlaybackStateCompat.Builder()
            stateBuilder.setActions(actions)
            stateBuilder.setState(
                state,
                mediaPlayer!!.currentPosition.toLong(),
                1.0f,
                SystemClock.elapsedRealtime()
            )
            return stateBuilder.build()
        }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if ("android.intent.action.MEDIA_BUTTON" == intent.action) {
            val broadcastManager = LocalBroadcastManager.getInstance(this)
            broadcastManager.registerReceiver(messageReceiver, IntentFilter(audioIntentForService))
            val keyEvent = intent.extras!!["android.intent.extra.KEY_EVENT"] as KeyEvent?
            mMediaNotificationManager?.apply {
                mediaPlayer?.let { mediaPlayer ->
                    builder?.let { builder ->
                        val currentDuration: Int = mediaPlayer.currentPosition
                        builder.setContentTitle(milliSecondsToTimer(currentDuration))

                        builder.clearActions()
                        if (keyEvent!!.keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                            mediaPlayer.pause()
                            Log.e("switch onstart", "pause")

                            broadcastManager.sendBroadcast(Intent(audioIntent).also {
                                it.putExtra("switch", "pause")
                            })

                            builder.addAction(mPlayAction)
                        } else {
                            mediaPlayer.start()
                            broadcastManager.sendBroadcast(Intent(audioIntent).also {
                                it.putExtra("switch", "play")
                            })

                            builder.addAction(mPauseAction)
                        }
                        updateSeekBar()

                        val notification = builder.build()
                        notificationManager.notify(
                            NOTIFICATION_ID,
                            notification
                        )
                    }
                }
            }
        } else {
            val practice = intent.getSerializableExtra("practice") as Practice
            val path = MusicRepository(application).getMusicFileFor(
                practice.courseId,
                practice.audioName
            ).absolutePath

            if ((mediaPlayer?.isPlaying != true) || path != lastAudioPath) {
                lastAudioPath = path
                mediaPlayer?.also { mediaPlayer ->
                    mediaPlayer.reset()
                    mMediaNotificationManager =
                        MediaNotificationManager(this, practice).also { manager ->
                            mediaSession = MediaSessionCompat(this, "MediaSessionCompat").apply {
                                setCallback(object : MediaSessionCompat.Callback() {
                                    override fun onPlay() {
                                        mediaPlayer.start()
                                    }

                                    override fun onPause() {
                                        mediaPlayer.pause()
                                    }
                                })
                                mediaPlayer.apply {
                                    setDataSource(path)
                                    isLooping = false
                                    prepare()
                                    start()
                                }
                                setNotification(manager)
                            }
                        }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun MediaSessionCompat.setNotification(manager: MediaNotificationManager) {
        val notification = manager.getNotification(
            metadata, state, sessionToken
        )
        startForeground(NOTIFICATION_ID, notification)
        updateSeekBar()
    }

    private val updater = Runnable {
        if (mediaPlayer?.isPlaying == true) {
            val currentDuration: Int = mediaPlayer!!.currentPosition
            val time = milliSecondsToTimer(currentDuration)
            mMediaNotificationManager?.builder?.setContentTitle(milliSecondsToTimer(currentDuration))
            val notification = mMediaNotificationManager?.builder?.build()
            mMediaNotificationManager?.notificationManager?.notify(NOTIFICATION_ID, notification)

            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(audioIntent).also {
                it.putExtra("time", time)
            })

            updateSeekBar()
        }
    }


    private fun updateSeekBar() {
        if (mediaPlayer?.isPlaying == true) {
            handler.postDelayed(updater, 1000)
        }
    }

    private fun milliSecondsToTimer(milliSeconds: Int): String {
        var timerString = ""
        val secondsString: String
        val hours = milliSeconds / (1000 * 60 * 60)
        val minutes = (milliSeconds % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = milliSeconds % (1000 * 60 * 60) % (1000 * 60) / 1000
        if (hours > 0) {
            timerString = "$hours:"
        }
        secondsString = if (seconds < 10) {
            "0$seconds"
        } else {
            "" + seconds
        }
        timerString = "$timerString$minutes:$secondsString"
        return timerString
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        const val NOTIFICATION_ID = 888
    }
}