package com.sumit.launcher.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.sumit.launcher.R

class FocusCheckService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private var checkRunnable: Runnable? = null
    private val screenReceiver = ScreenReceiver()
    private val powerManager by lazy {
        getSystemService(Context.POWER_SERVICE) as PowerManager
    }
    private var sessionStartMs: Long = 0L

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundCompat()
        ContextCompat.registerReceiver(
            this,
            screenReceiver,
            IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_ON)
                addAction(Intent.ACTION_SCREEN_OFF)
                addAction(Intent.ACTION_USER_PRESENT)
            },
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        if (powerManager.isInteractive) {
            sessionStartMs = System.currentTimeMillis()
            scheduleCheck()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelCheck()
        FocusCheckOverlay.hide(this)
        runCatching { unregisterReceiver(screenReceiver) }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundCompat() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_view)
            .setContentTitle(getString(R.string.focus_check_service_title))
            .setContentText(getString(R.string.focus_check_service_body))
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = getSystemService(NotificationManager::class.java) ?: return
        if (nm.getNotificationChannel(CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.focus_check_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.focus_check_channel_description)
                setShowBadge(false)
            }
            nm.createNotificationChannel(channel)
        }
        if (nm.getNotificationChannel(ALERT_CHANNEL_ID) == null) {
            val alertChannel = NotificationChannel(
                ALERT_CHANNEL_ID,
                getString(R.string.focus_check_alert_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.focus_check_alert_channel_description)
            }
            nm.createNotificationChannel(alertChannel)
        }
    }

    private fun notifyOverlayPermissionMissing() {
        val nm = getSystemService(NotificationManager::class.java) ?: return
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pi = PendingIntent.getActivity(this, 0, intent, flags)
        val notification = NotificationCompat.Builder(this, ALERT_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(getString(R.string.focus_check_permission_title))
            .setContentText(getString(R.string.focus_check_permission_body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(pi)
            .build()
        nm.notify(ALERT_NOTIFICATION_ID, notification)
    }

    private fun scheduleCheck() {
        cancelCheck()
        val runnable = Runnable {
            checkRunnable = null
            if (powerManager.isInteractive) {
                val elapsedMin = ((System.currentTimeMillis() - sessionStartMs) / 60_000L)
                    .toInt()
                    .coerceAtLeast(15)
                if (!Settings.canDrawOverlays(this)) {
                    notifyOverlayPermissionMissing()
                    scheduleCheck()
                    return@Runnable
                }
                FocusCheckOverlay.show(
                    this,
                    elapsedMin,
                    onContinue = {
                        FocusCheckOverlay.hide(this)
                        scheduleCheck()
                    },
                    onStepAway = {
                        FocusCheckOverlay.hide(this)
                        goHome()
                        // Reset session so the launcher dwelling doesn't immediately
                        // trigger the next overlay 15 min later.
                        sessionStartMs = System.currentTimeMillis()
                        scheduleCheck()
                    }
                )
            }
        }
        checkRunnable = runnable
        handler.postDelayed(runnable, INTERVAL_MS)
    }

    private fun goHome() {
        val homeIntent = Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_HOME)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        runCatching { startActivity(homeIntent) }
    }

    private fun cancelCheck() {
        checkRunnable?.let { handler.removeCallbacks(it) }
        checkRunnable = null
    }

    private inner class ScreenReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    cancelCheck()
                    FocusCheckOverlay.hide(this@FocusCheckService)
                    sessionStartMs = 0L
                }
                Intent.ACTION_USER_PRESENT,
                Intent.ACTION_SCREEN_ON -> {
                    if (sessionStartMs == 0L) {
                        sessionStartMs = System.currentTimeMillis()
                        scheduleCheck()
                    }
                }
            }
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 4242
        private const val ALERT_NOTIFICATION_ID = 4243
        private const val CHANNEL_ID = "focus_check"
        private const val ALERT_CHANNEL_ID = "focus_check_alert"
        private const val INTERVAL_MS = 15L * 60L * 1000L

        fun start(context: Context) {
            ContextCompat.startForegroundService(
                context,
                Intent(context, FocusCheckService::class.java)
            )
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, FocusCheckService::class.java))
        }
    }
}
