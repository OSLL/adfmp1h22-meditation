package ru.hse.meditation.ui.notifications

import android.app.*
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
import android.view.KeyEvent
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media.session.MediaButtonReceiver
import kotlinx.coroutines.runBlocking
import ru.hse.meditation.R
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.model.entity.PracticeRecord
import ru.hse.meditation.model.repository.MusicRepository
import ru.hse.meditation.model.repository.PracticeRecordRepository
import ru.hse.meditation.model.repository.PracticeRepository
import ru.hse.meditation.ui.history.edit.EditPracticeRecordActivity
import ru.hse.meditation.ui.meditations.audioIntent
import java.util.*


const val audioIntentForService = "audioIntentForService"

class MediaSessionService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var mMediaNotificationManager: MediaNotificationManager? = null
    private var mediaSession: MediaSessionCompat? = null
    private var lastAudioPath: String? = null
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var practice: Practice? = null
    private var timeLeft: Int? = null

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
                            mMediaNotificationManager?.notificationManager?.cancelAll()
                            stopForeground(true)
                            stopSelf()

                            LocalBroadcastManager.getInstance(this@MediaSessionService)
                                .sendBroadcast(Intent(audioIntent).also {
                                    it.putExtra("switch", "stop")
                                })
                        }
                    }
                }
            }
        }
    }

    private fun endPractice() {
        practice?.let { practice ->
            runBlocking {
                val date = Date()
                val record = PracticeRecord(
                    practice.courseId,
                    practice.name,
                    date,
                    practice.duration,
                    ""
                )
                val id = PracticeRecordRepository(Application()).insert(
                    record
                ).toInt()

                practice.lastPracticeDateTime = date
                PracticeRepository(Application()).update(practice)

                mediaPlayer!!.reset()
                mMediaNotificationManager!!.notificationManager.cancelAll()
                stopForeground(true)
                stopSelf()

                sendFinishNotification(record.copy(id = id))

                LocalBroadcastManager.getInstance(this@MediaSessionService)
                    .sendBroadcast(Intent(audioIntent).also {
                        it.putExtra("switch", "finish")
                    })
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setOnCompletionListener { mediaPlayer ->
            timeLeft = timeLeft!! - mediaPlayer.duration

            if (timeLeft!! < 0) {
                endPractice()
            }
        }
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
            practice = (intent.getSerializableExtra("practice") as Practice)
            timeLeft = practice!!.duration * 60000 / 100
            val path = MusicRepository(application).getMusicFileFor(
                practice!!.courseId,
                practice!!.audioName
            ).absolutePath

            if ((mediaPlayer?.isPlaying != true) || path != lastAudioPath) {
                lastAudioPath = path
                mediaPlayer?.also { mediaPlayer ->
                    mediaPlayer.reset()
                    mMediaNotificationManager =
                        MediaNotificationManager(this, practice!!).also { manager ->
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
                                    isLooping = true
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
            if (timeLeft!! < currentDuration) {
                endPractice()
            }

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

    private fun sendFinishNotification(record: PracticeRecord) {
        val resultIntent = Intent(this, EditPracticeRecordActivity::class.java)
        resultIntent.putExtra("record", record)
        val resultPendingIntent = PendingIntent.getActivity(
            this, 0, resultIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        getNotificationBuilder(this).apply {
            setSmallIcon(R.drawable.ic_baseline_self_improvement_24)
            setContentTitle(getString(R.string.you_have_completed).format(record.practiceName))
            setContentText(getString(R.string.click_to_edit_record))
            setContentIntent(resultPendingIntent)
            setAutoCancel(true)
        }.build().also { getNotificationManager().notify(889, it) }
    }

    private fun getNotificationBuilder(context: Context): NotificationCompat.Builder {
        val channel = getNotificationChannel()
        return NotificationCompat.Builder(context, channel.id)
    }

    private fun getNotificationChannel(): NotificationChannel {
        val notificationManager = getNotificationManager()
        return notificationManager.getNotificationChannel("3939") ?: run {
            val channelId = "3939"
            val name = "HSE meditations channel"
            val description = "Notification about finishing meditation"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.enableLights(true)
            channel.lightColor = getColor(R.color.meditation)
            notificationManager.createNotificationChannel(channel)
            return channel
        }
    }

    private fun getNotificationManager(): NotificationManager {
        return getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    companion object {
        const val NOTIFICATION_ID = 888
    }
}
