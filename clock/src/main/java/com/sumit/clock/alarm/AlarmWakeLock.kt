package com.sumit.clock.alarm

import android.content.Context
import android.os.PowerManager

internal object AlarmWakeLock {

    private const val TAG = "AlarmReceiver:fire"
    private const val TIMEOUT_MS = 60_000L

    @Volatile private var wakeLock: PowerManager.WakeLock? = null

    @Synchronized
    fun acquire(context: Context) {
        if (wakeLock?.isHeld == true) return
        val pm = context.applicationContext.getSystemService(Context.POWER_SERVICE) as? PowerManager
            ?: return
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG).apply {
            setReferenceCounted(false)
        }
        runCatching { wl.acquire(TIMEOUT_MS) }
        wakeLock = wl
    }

    @Synchronized
    fun release() {
        val wl = wakeLock ?: return
        wakeLock = null
        runCatching { if (wl.isHeld) wl.release() }
    }
}
