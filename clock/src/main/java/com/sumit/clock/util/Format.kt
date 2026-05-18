package com.sumit.clock.util

/** mm:ss.SS for sub-hour, hh:mm:ss.S for ≥ 1 h. */
fun formatStopwatch(ms: Long): String {
    val totalCentis = ms / 10L
    val centi = totalCentis % 100L
    val totalSec = totalCentis / 100L
    val sec = totalSec % 60L
    val totalMin = totalSec / 60L
    val min = totalMin % 60L
    val hr = totalMin / 60L
    return if (hr > 0) "%02d:%02d:%02d.%01d".format(hr, min, sec, centi / 10L)
    else "%02d:%02d.%02d".format(min, sec, centi)
}

/** hh:mm:ss countdown style. */
fun formatTimer(ms: Long): String {
    val totalSec = (ms + 500L) / 1000L
    val sec = totalSec % 60L
    val totalMin = totalSec / 60L
    val min = totalMin % 60L
    val hr = totalMin / 60L
    return "%02d:%02d:%02d".format(hr, min, sec)
}
