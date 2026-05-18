package com.sumit.launcher.domain.focus

import android.content.Context
import com.sumit.launcher.data.usage.UsageStatsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Decides whether the user should be shown a "focus check-in" dialog right now.
 * Returns the current session length in minutes if a check is due, else null.
 *
 * Cadence: every [INTERVAL_MS] of continuous screen-on time. A new prompt fires
 * 15 min into the session, then every 15 min after the previous prompt as long
 * as the screen stays continuously on.
 */
@Singleton
class FocusCheckUseCase @Inject constructor(
    @ApplicationContext context: Context,
    private val usage: UsageStatsRepository
) {
    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun checkDue(): Int? {
        val sessionStart = usage.currentScreenOnSince() ?: return null
        val now = System.currentTimeMillis()
        val lastPrompt = prefs.getLong(KEY_LAST_PROMPT, 0L)
        val anchor = maxOf(lastPrompt, sessionStart)
        val elapsedSinceAnchor = now - anchor
        if (elapsedSinceAnchor < INTERVAL_MS) return null
        val totalSessionMinutes = ((now - sessionStart) / 60_000L).toInt()
        return totalSessionMinutes
    }

    fun markShown() {
        prefs.edit().putLong(KEY_LAST_PROMPT, System.currentTimeMillis()).apply()
    }

    private companion object {
        const val PREFS = "focus_check"
        const val KEY_LAST_PROMPT = "last_prompt_ms"
        const val INTERVAL_MS = 15L * 60_000L
    }
}
