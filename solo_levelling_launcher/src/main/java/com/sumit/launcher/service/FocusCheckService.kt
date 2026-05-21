package com.sumit.launcher.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.sumit.launcher.R
import com.sumit.launcher.data.focus.AppFocusRepository
import com.sumit.launcher.data.focus.FocusOverlayRepository
import com.sumit.launcher.data.usage.UsageStatsRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Foreground service that watches the current foreground app and shows the focus
 * check-in overlay when an app passes the daily limit the user set for it.
 *
 * It polls once a minute: it resolves the foreground package, looks up that app's
 * configured daily limit, and compares it against today's usage for that package.
 */
@AndroidEntryPoint
class FocusCheckService : Service() {

    @Inject
    lateinit var appFocusRepository: AppFocusRepository

    @Inject
    lateinit var usageStatsRepository: UsageStatsRepository

    private val mainHandler = Handler(Looper.getMainLooper())
    private var pollThread: HandlerThread? = null

    @Volatile
    private var pollHandler: Handler? = null

    private val powerManager by lazy {
        getSystemService(Context.POWER_SERVICE) as PowerManager
    }

    /** Touched only on the main thread. */
    private var overlayShownFor: String? = null
    private val snoozeUntil = HashMap<String, Long>()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundCompat()
        startPolling()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPolling()
        FocusCheckOverlay.hide(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // ───────────────────────────── polling ─────────────────────────────

    private fun startPolling() {
        if (pollThread != null) return
        val thread = HandlerThread("focus-check-poll").apply { start() }
        pollThread = thread
        pollHandler = Handler(thread.looper).also {
            it.postDelayed(::poll, FIRST_POLL_DELAY_MS)
        }
    }

    private fun stopPolling() {
        pollHandler?.removeCallbacksAndMessages(null)
        mainHandler.removeCallbacksAndMessages(null)
        pollThread?.quitSafely()
        pollThread = null
        pollHandler = null
    }

    /** Runs on the poll background thread — usage queries must stay off the main thread. */
    private fun poll() {
        if (powerManager.isInteractive) {
            val pkg = usageStatsRepository.currentForegroundPackage()
            val limit = pkg
                ?.takeIf { it != packageName }
                ?.let { appFocusRepository.state.value.entryFor(it).dailyLimitMinutes }
            val used = if (pkg != null && limit != null) {
                usageStatsRepository.getTodayUsagePerPackage()[pkg] ?: 0
            } else {
                0
            }
            mainHandler.post { onPollResult(pkg, limit, used) }
        }
        pollHandler?.postDelayed(::poll, POLL_INTERVAL_MS)
    }

    /** Runs on the main thread — owns the overlay and snooze state. */
    private fun onPollResult(foregroundPackage: String?, limitMinutes: Int?, usedMinutes: Int) {
        // Not inside a limit-tracked app — clear any stale overlay.
        if (foregroundPackage == null || limitMinutes == null) {
            dismissOverlay()
            return
        }
        // Switched to a different app than the overlay belongs to.
        if (overlayShownFor != null && overlayShownFor != foregroundPackage) {
            dismissOverlay()
        }
        if (usedMinutes < limitMinutes) return
        if (overlayShownFor == foregroundPackage) return  // already showing
        if (System.currentTimeMillis() < (snoozeUntil[foregroundPackage] ?: 0L)) return

        if (!Settings.canDrawOverlays(this)) {
            notifyOverlayPermissionMissing()
            return
        }

        FocusCheckOverlay.show(
            context = this,
            appLabel = appLabel(foregroundPackage),
            usedMinutes = usedMinutes,
            onContinue = {
                dismissOverlay()
                snoozeUntil[foregroundPackage] = System.currentTimeMillis() + SNOOZE_MS
            },
            onStepAway = {
                dismissOverlay()
                snoozeUntil[foregroundPackage] = System.currentTimeMillis() + SNOOZE_MS
                goHome()
            }
        )
        overlayShownFor = foregroundPackage
    }

    private fun dismissOverlay() {
        if (overlayShownFor != null) {
            FocusCheckOverlay.hide(this)
            overlayShownFor = null
        }
    }

    private fun appLabel(packageName: String): String = runCatching {
        val pm = packageManager
        pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0)).toString()
    }.getOrDefault(packageName)

    private fun goHome() {
        val homeIntent = Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_HOME)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        runCatching { startActivity(homeIntent) }
    }

    // ──────────────────────────── notifications ────────────────────────────

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

    companion object {
        private const val NOTIFICATION_ID = 4242
        private const val ALERT_NOTIFICATION_ID = 4243
        private const val CHANNEL_ID = "focus_check"
        private const val ALERT_CHANNEL_ID = "focus_check_alert"

        private const val POLL_INTERVAL_MS = 60L * 1000L
        private const val FIRST_POLL_DELAY_MS = 5L * 1000L

        /** After "I'm on track" / "Step away", wait this long before re-prompting for the app. */
        private const val SNOOZE_MS = 10L * 60L * 1000L

        fun start(context: Context) {
            ContextCompat.startForegroundService(
                context,
                Intent(context, FocusCheckService::class.java)
            )
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, FocusCheckService::class.java))
        }

        /**
         * Starts the service only if the user has the overlay enabled. Safe to call from
         * a foreground activity on every launch — the service isn't restarted on reboot
         * on its own, so this re-arms it once the launcher process is back.
         */
        fun startIfEnabled(context: Context) {
            val enabled = context
                .getSharedPreferences(FocusOverlayRepository.PREFS, Context.MODE_PRIVATE)
                .getBoolean(FocusOverlayRepository.KEY_ENABLED, false)
            if (enabled) start(context)
        }
    }
}
