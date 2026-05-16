package com.sumit.launcher.data.focus

import java.util.Calendar
import java.util.UUID

/**
 * A scheduled focus window. While active, apps flagged for the focus prompt
 * get a longer countdown (see [com.sumit.launcher.data.launch.AppLaunchPolicy]).
 *
 * Times are minutes since midnight (0..1439). Days are a 7-bit bitmask with
 * Monday at bit 0 and Sunday at bit 6.
 */
data class FocusBlock(
    val id: String = UUID.randomUUID().toString(),
    val startMinute: Int,
    val endMinute: Int,
    val daysMask: Int,
    val enabled: Boolean = true
) {
    fun isActive(at: Long = System.currentTimeMillis()): Boolean {
        if (!enabled || daysMask == 0) return false
        val cal = Calendar.getInstance().apply { timeInMillis = at }
        val bit = when (cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> return false
        }
        if ((daysMask and (1 shl bit)) == 0) return false
        val minute = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
        return if (startMinute <= endMinute) {
            minute in startMinute until endMinute
        } else {
            // Overnight block (e.g. 22:00 to 06:00).
            minute >= startMinute || minute < endMinute
        }
    }

    companion object {
        const val ALL_DAYS_MASK = 0b1111111
        const val WEEKDAYS_MASK = 0b0011111  // Mon..Fri
        const val WEEKEND_MASK = 0b1100000   // Sat..Sun
    }
}
