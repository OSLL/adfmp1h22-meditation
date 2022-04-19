package ru.hse.meditation.ui.notifications

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.session.MediaButtonReceiver
import ru.hse.meditation.R
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.ui.meditations.MeditationActivity

/**
 * Keeps track of a notification and updates it automatically for a given MediaSession. This is
 * required so that the music service don't get killed during playback.
 */
class MediaNotificationManager(private val mService: MediaSessionService, val practice: Practice) {
    var builder: NotificationCompat.Builder? = null

    val mPlayAction: NotificationCompat.Action = NotificationCompat.Action(
        R.drawable.ic_play,
        "play",
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            mService,
            PlaybackStateCompat.ACTION_PLAY
        )
    )
    val mPauseAction: NotificationCompat.Action = NotificationCompat.Action(
        R.drawable.ic_pause,
        "pause",
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            mService,
            PlaybackStateCompat.ACTION_PAUSE
        )
    )
    val notificationManager: NotificationManager =
        mService.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

    fun getNotification(
        metadata: MediaMetadataCompat,
        state: PlaybackStateCompat,
        token: MediaSessionCompat.Token
    ): Notification {
        val isPlaying = state.state == PlaybackStateCompat.STATE_PLAYING
        val description = metadata.description
        buildNotification(token, isPlaying, description).also {
            builder = it
            return it.build()
        }
    }


    private fun buildNotification(
        token: MediaSessionCompat.Token,
        isPlaying: Boolean,
        description: MediaDescriptionCompat
    ): NotificationCompat.Builder {

        createChannel()
        val builder = NotificationCompat.Builder(mService, CHANNEL_ID)
        builder.setStyle(
            androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(token)
                .setShowActionsInCompactView(0) // For backwards compatibility with Android L and earlier.
                .setShowCancelButton(true)
                .setCancelButtonIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        mService,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )
        )
            .setColor(ContextCompat.getColor(mService, R.color.meditation))
            .setSmallIcon(R.drawable.ic_play) // Pending intent that is fired when user clicks on notification.
            .setContentIntent(createContentIntent()) // Title - Usually Song name.
            .setContentTitle(description.title) // When notification is deleted (when playback is paused and notification can be
            // deleted) fire MediaButtonPendingIntent with ACTION_PAUSE.
            .setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    mService, PlaybackStateCompat.ACTION_PAUSE
                )
            )
        builder.addAction(if (isPlaying) mPauseAction else mPlayAction)
        return builder
    }

    // Does nothing on versions of Android earlier than O.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            // The user-visible name of the channel.
            val name: CharSequence = "MediaSession"
            // The user-visible description of the channel.
            val description = "MediaSession and MediaPlayer"
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            // Configure the notification channel.
            mChannel.description = description
            mChannel.enableLights(true)
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(mChannel)
            Log.d(TAG, "createChannel: New channel created")
        } else {
            Log.d(TAG, "createChannel: Existing channel reused")
        }
    }


    private fun createContentIntent(): PendingIntent {
        val intent = Intent(mService, MeditationActivity::class.java)
        intent.putExtra("practice", practice)
        intent.putExtra("serviceOn", true)

        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(
            mService, REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        private val TAG = MediaNotificationManager::class.java.simpleName
        private const val CHANNEL_ID = "com.example.android.musicplayer.channel"
        private const val REQUEST_CODE = 501
    }

    init {
        // Cancel all notifications to handle the case where the Service was killed and
        // restarted by the system.
        notificationManager.cancelAll()
    }
}