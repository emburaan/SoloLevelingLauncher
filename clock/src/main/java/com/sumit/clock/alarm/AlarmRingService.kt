package com.sumit.clock.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.sumit.clock.R

class AlarmRingService : Service() {

    private var ringer: MediaPlayer? = null
    private var lockedAlarmVolume: Int = -1

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                stopRinging()
                AlarmWakeLock.release()
                return START_NOT_STICKY
            }
        }

        val extras = intent
        if (extras == null) {
            AlarmWakeLock.release()
            return START_NOT_STICKY
        }
        val alarmId = extras.getLongExtra(AlarmReceiver.EXTRA_ALARM_ID, -1L)
        val label = extras.getStringExtra(AlarmReceiver.EXTRA_ALARM_LABEL).orEmpty()

        startForegroundCompat(label)
        lockAlarmVolume()
        startRinger()
        launchRingActivity(extras, alarmId)
        AlarmWakeLock.release()
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        stopRinging()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundCompat(label: String) {
        val fullScreen = Intent(this, AlarmRingActivity::class.java).addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        )
        val pendingFlags =
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val contentPi = PendingIntent.getActivity(this, 0, fullScreen, pendingFlags)

        val title = getString(R.string.alarm_ring_title)
        val body = if (label.isBlank()) title else label
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(body)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(contentPi)
            .setFullScreenIntent(contentPi, true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = getSystemService(NotificationManager::class.java) ?: return
        if (nm.getNotificationChannel(CHANNEL_ID) != null) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.alarm_ring_title),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setShowBadge(false)
            setSound(null, null)
            enableVibration(false)
            setBypassDnd(true)
        }
        nm.createNotificationChannel(channel)
    }

    private fun launchRingActivity(source: Intent, alarmId: Long) {
        if (alarmId < 0) return
        val ring = Intent(this, AlarmRingActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
            putExtras(source)
        }
        runCatching { startActivity(ring) }
    }

    private fun startRinger() {
        if (ringer != null) return
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ?: return
        runCatching {
            ringer = MediaPlayer().apply {
                setDataSource(this@AlarmRingService, uri)
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                isLooping = true
                prepare()
                start()
            }
        }
    }

    private fun stopRinging() {
        ringer?.let { mp ->
            runCatching { mp.stop() }
            runCatching { mp.release() }
        }
        ringer = null
        lockedAlarmVolume = -1
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun lockAlarmVolume() {
        val am = getSystemService(Context.AUDIO_SERVICE) as? AudioManager ?: return
        runCatching {
            lockedAlarmVolume = am.getStreamMaxVolume(AudioManager.STREAM_ALARM)
            am.setStreamVolume(AudioManager.STREAM_ALARM, lockedAlarmVolume, 0)
        }
    }

    companion object {
        const val ACTION_STOP = "com.sumit.clock.action.ALARM_STOP"
        private const val CHANNEL_ID = "alarm_ring"
        private const val NOTIFICATION_ID = 7474

        fun start(context: Context, fireIntent: Intent) {
            val intent = Intent(context, AlarmRingService::class.java).apply {
                putExtras(fireIntent)
            }
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, AlarmRingService::class.java).apply {
                action = ACTION_STOP
            }
            runCatching { context.startService(intent) }
        }
    }
}
