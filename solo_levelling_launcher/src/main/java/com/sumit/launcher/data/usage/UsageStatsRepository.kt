package com.sumit.launcher.data.usage

import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.os.Process
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max
import kotlin.math.min

@Singleton
class UsageStatsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun hasUsageAccess(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * Returns 7 entries — index 0 is 6 days ago, index 6 is today. Empty list if no access.
     *
     * Uses queryEvents to pair MOVE_TO_FOREGROUND/MOVE_TO_BACKGROUND events so overlap
     * between concurrently-recorded packages isn't double-counted.
     */
    fun getLastSevenDays(): List<DayUsage> {
        if (!hasUsageAccess()) return emptyList()

        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            ?: return emptyList()

        val dayLetters = arrayOf("S", "M", "T", "W", "T", "F", "S")
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val todayStart = cal.timeInMillis
        val msPerDay = 24L * 60L * 60L * 1000L
        val windowStart = todayStart - 6L * msPerDay
        val now = System.currentTimeMillis()
        val msPerBucket = LongArray(7)

        fun addUsage(rawStart: Long, rawEnd: Long) {
            if (rawEnd <= rawStart) return
            var s = max(rawStart, windowStart)
            val e = min(rawEnd, now)
            while (s < e) {
                val bucketIdx = ((s - windowStart) / msPerDay).toInt().coerceIn(0, 6)
                val bucketEnd = windowStart + (bucketIdx + 1) * msPerDay
                val sliceEnd = min(e, bucketEnd)
                msPerBucket[bucketIdx] += sliceEnd - s
                s = sliceEnd
            }
        }

        val events = runCatching { usm.queryEvents(windowStart, now) }.getOrNull()
            ?: return emptyList()
        val ev = UsageEvents.Event()

        // Sum screen-on durations. A session starts when SCREEN_INTERACTIVE fires
        // and ends on SCREEN_NON_INTERACTIVE (or as a safety net, the 6-hour cap).
        var sessionStart: Long? = null

        while (events.hasNextEvent()) {
            events.getNextEvent(ev)
            when (ev.eventType) {
                EVENT_SCREEN_INTERACTIVE -> {
                    if (sessionStart == null) sessionStart = ev.timeStamp
                }
                EVENT_SCREEN_NON_INTERACTIVE -> {
                    val start = sessionStart
                    if (start != null) {
                        addUsage(start, cappedEnd(start, ev.timeStamp))
                    } else {
                        // Screen was already on at windowStart — count the partial.
                        addUsage(windowStart, cappedEnd(windowStart, ev.timeStamp))
                    }
                    sessionStart = null
                }
            }
        }
        val open = sessionStart
        if (open != null) addUsage(open, cappedEnd(open, now))

        return (0..6).map { idx ->
            val dayStart = windowStart + idx * msPerDay
            val dayCal = Calendar.getInstance().apply { timeInMillis = dayStart }
            DayUsage(
                label = dayLetters[dayCal.get(Calendar.DAY_OF_WEEK) - 1],
                minutes = (msPerBucket[idx] / 60_000L).toInt()
            )
        }
    }

    /**
     * Returns today's foreground time per package, in minutes. Uses the same FG/BG event
     * pairing as [getLastSevenDays] so concurrently-recorded packages aren't double-counted.
     */
    fun getTodayUsagePerPackage(): Map<String, Int> {
        if (!hasUsageAccess()) return emptyMap()

        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            ?: return emptyMap()

        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val todayStart = cal.timeInMillis
        val now = System.currentTimeMillis()

        val events = runCatching { usm.queryEvents(todayStart, now) }.getOrNull()
            ?: return emptyMap()
        val ev = UsageEvents.Event()
        var fgPackage: String? = null
        var fgStart: Long = 0L
        val msByPackage = HashMap<String, Long>()

        fun addUsage(pkg: String, rawStart: Long, rawEnd: Long) {
            if (rawEnd <= rawStart) return
            val s = max(rawStart, todayStart)
            val e = min(rawEnd, now)
            if (e > s) msByPackage.merge(pkg, e - s) { a, b -> a + b }
        }

        @Suppress("DEPRECATION")
        while (events.hasNextEvent()) {
            events.getNextEvent(ev)
            when (ev.eventType) {
                UsageEvents.Event.MOVE_TO_FOREGROUND -> {
                    val open = fgPackage
                    if (open != null) {
                        addUsage(open, fgStart, cappedEnd(fgStart, ev.timeStamp))
                    }
                    fgPackage = ev.packageName
                    fgStart = ev.timeStamp
                }
                UsageEvents.Event.MOVE_TO_BACKGROUND,
                EVENT_SCREEN_NON_INTERACTIVE,
                EVENT_KEYGUARD_SHOWN -> {
                    val open = fgPackage
                    if (open != null) {
                        addUsage(open, fgStart, cappedEnd(fgStart, ev.timeStamp))
                        fgPackage = null
                    }
                }
            }
        }
        val stillOpen = fgPackage
        if (stillOpen != null) addUsage(stillOpen, fgStart, cappedEnd(fgStart, now))

        return msByPackage.mapValues { (it.value / 60_000L).toInt() }
    }

    /**
     * Today's screen-on stats: how many times the screen woke up and the total minutes on.
     * Counts SCREEN_INTERACTIVE events as pickups; sums SCREEN_INTERACTIVE → SCREEN_NON_INTERACTIVE
     * pairs for total minutes (with the same 6-hour cap as [getLastSevenDays]).
     */
    fun getTodayScreenStats(): TodayScreenStats {
        if (!hasUsageAccess()) return TodayScreenStats(pickups = 0, minutes = 0)

        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            ?: return TodayScreenStats(pickups = 0, minutes = 0)

        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val todayStart = cal.timeInMillis
        val now = System.currentTimeMillis()

        val events = runCatching { usm.queryEvents(todayStart, now) }.getOrNull()
            ?: return TodayScreenStats(pickups = 0, minutes = 0)
        val ev = UsageEvents.Event()
        var pickups = 0
        var totalMs = 0L
        var sessionStart: Long? = null

        while (events.hasNextEvent()) {
            events.getNextEvent(ev)
            when (ev.eventType) {
                EVENT_SCREEN_INTERACTIVE -> {
                    pickups++
                    if (sessionStart == null) sessionStart = ev.timeStamp
                }
                EVENT_SCREEN_NON_INTERACTIVE -> {
                    val start = sessionStart
                    if (start != null) {
                        totalMs += cappedEnd(start, ev.timeStamp) - start
                    } else {
                        totalMs += cappedEnd(todayStart, ev.timeStamp) - todayStart
                    }
                    sessionStart = null
                }
            }
        }
        val open = sessionStart
        if (open != null) totalMs += cappedEnd(open, now) - open

        return TodayScreenStats(
            pickups = pickups,
            minutes = (totalMs / 60_000L).toInt()
        )
    }

    /**
     * Returns the timestamp of the most recent SCREEN_INTERACTIVE event with no later
     * SCREEN_NON_INTERACTIVE — i.e. when the current continuous screen-on session started.
     * Returns null if the screen is off, no event found in the lookback window, or no access.
     */
    fun currentScreenOnSince(): Long? {
        if (!hasUsageAccess()) return null
        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            ?: return null

        val now = System.currentTimeMillis()
        val windowStart = now - 24L * 60L * 60L * 1000L  // look back 24 hours

        val events = runCatching { usm.queryEvents(windowStart, now) }.getOrNull()
            ?: return null
        val ev = UsageEvents.Event()
        var sessionStart: Long? = null

        while (events.hasNextEvent()) {
            events.getNextEvent(ev)
            when (ev.eventType) {
                EVENT_SCREEN_INTERACTIVE -> if (sessionStart == null) sessionStart = ev.timeStamp
                EVENT_SCREEN_NON_INTERACTIVE -> sessionStart = null
            }
        }
        return sessionStart
    }

    private companion object {
        // UsageEvents.Event constants — named constants exist on API 28+, but the
        // underlying events themselves fire on earlier API levels too. Hardcoded
        // values stay stable across releases.
        const val EVENT_SCREEN_INTERACTIVE = 15
        const val EVENT_SCREEN_NON_INTERACTIVE = 16
        const val EVENT_KEYGUARD_SHOWN = 17

        /** Cap a single foreground session at 6 hours to defend against missing end events. */
        const val MAX_SESSION_MS = 6L * 60L * 60L * 1000L

        fun cappedEnd(start: Long, end: Long): Long =
            if (end - start > MAX_SESSION_MS) start + MAX_SESSION_MS else end
    }
}
