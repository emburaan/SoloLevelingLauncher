package com.sumit.launcher.data.focus

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

data class AppFocusEntry(
    val requirePrompt: Boolean = false,
    /** Per-day allowance in effect today, or null if there's no (active) limit. */
    val dailyLimitMinutes: Int? = null,
    /** Whole days the limit still applies, or null if it has no expiry. */
    val limitDaysRemaining: Int? = null
)

/**
 * A per-app daily time limit. [minutesPerDay] is the allowance per day; the limit
 * stays in force until [expiresAtMillis] (a local-midnight timestamp), or forever
 * when [expiresAtMillis] is [NO_EXPIRY].
 */
data class AppLimit(
    val minutesPerDay: Int,
    val expiresAtMillis: Long = NO_EXPIRY
) {
    companion object {
        const val NO_EXPIRY = 0L
    }
}

data class AppFocusState(
    /** Apps the user has explicitly turned on. */
    val promptPackages: Set<String> = emptySet(),
    /** Default distractors the user has explicitly turned off. */
    val disabledDefaults: Set<String> = emptySet(),
    val dailyLimits: Map<String, AppLimit> = emptyMap(),
    val focusBlocks: List<FocusBlock> = emptyList()
) {
    fun entryFor(packageName: String, now: Long = System.currentTimeMillis()): AppFocusEntry {
        val isDefault = packageName in DEFAULT_DISTRACTOR_PACKAGES
        val effective = packageName in promptPackages ||
            (isDefault && packageName !in disabledDefaults)

        val limit = dailyLimits[packageName]
        val active = limit != null &&
            (limit.expiresAtMillis == AppLimit.NO_EXPIRY || now < limit.expiresAtMillis)
        val daysRemaining = if (active && limit!!.expiresAtMillis != AppLimit.NO_EXPIRY) {
            ceil((limit.expiresAtMillis - now) / DAY_MS.toDouble()).toInt().coerceAtLeast(0)
        } else null

        return AppFocusEntry(
            requirePrompt = effective,
            dailyLimitMinutes = if (active) limit!!.minutesPerDay else null,
            limitDaysRemaining = daysRemaining
        )
    }

    fun activeFocusBlock(at: Long = System.currentTimeMillis()): FocusBlock? =
        focusBlocks.firstOrNull { it.isActive(at) }

    private companion object {
        const val DAY_MS = 86_400_000L
    }
}

@Singleton
class AppFocusRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _state = MutableStateFlow(load())
    val state: StateFlow<AppFocusState> = _state.asStateFlow()

    fun setRequirePrompt(packageName: String, enabled: Boolean) {
        val current = _state.value
        val isDefault = packageName in DEFAULT_DISTRACTOR_PACKAGES

        val newOn: Set<String>
        val newOff: Set<String>
        if (enabled) {
            // For a default distractor, the heuristic alone covers it — clear any explicit off.
            // For non-default apps, we must record an explicit on.
            newOn = if (isDefault) current.promptPackages - packageName
            else current.promptPackages + packageName
            newOff = current.disabledDefaults - packageName
        } else {
            // Off: clear any explicit on, and for defaults record an explicit off.
            newOn = current.promptPackages - packageName
            newOff = if (isDefault) current.disabledDefaults + packageName
            else current.disabledDefaults
        }

        prefs.edit()
            .putStringSet(KEY_PROMPT_PACKAGES, newOn)
            .putStringSet(KEY_DISABLED_DEFAULTS, newOff)
            .apply()
        _state.value = current.copy(
            promptPackages = newOn,
            disabledDefaults = newOff
        )
    }

    /**
     * Sets a per-day limit of [minutes] for [packageName], enforced for [days] days
     * from today (0 = no expiry / ongoing). Pass null/0 minutes to clear the limit.
     */
    fun setDailyLimit(packageName: String, minutes: Int?, days: Int) {
        val current = _state.value.dailyLimits
        val updated = if (minutes == null || minutes <= 0) {
            current - packageName
        } else {
            current + (packageName to AppLimit(minutes, expiryFor(days)))
        }
        prefs.edit().putString(KEY_LIMITS, encodeLimits(updated)).apply()
        _state.value = _state.value.copy(dailyLimits = updated)
    }

    /** Local-midnight timestamp [days] days from now, or [AppLimit.NO_EXPIRY] when days <= 0. */
    private fun expiryFor(days: Int): Long {
        if (days <= 0) return AppLimit.NO_EXPIRY
        return Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, days)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun setFocusBlocks(blocks: List<FocusBlock>) {
        prefs.edit().putString(KEY_FOCUS_BLOCKS, encodeBlocks(blocks)).apply()
        _state.value = _state.value.copy(focusBlocks = blocks)
    }

    fun upsertFocusBlock(block: FocusBlock) {
        val current = _state.value.focusBlocks
        val updated = if (current.any { it.id == block.id }) {
            current.map { if (it.id == block.id) block else it }
        } else {
            current + block
        }
        setFocusBlocks(updated)
    }

    fun deleteFocusBlock(id: String) {
        setFocusBlocks(_state.value.focusBlocks.filter { it.id != id })
    }

    private fun load(): AppFocusState = AppFocusState(
        promptPackages = prefs.getStringSet(KEY_PROMPT_PACKAGES, emptySet())
            ?.toSet().orEmpty(),
        disabledDefaults = prefs.getStringSet(KEY_DISABLED_DEFAULTS, emptySet())
            ?.toSet().orEmpty(),
        dailyLimits = decodeLimits(prefs.getString(KEY_LIMITS, null)),
        focusBlocks = decodeBlocks(prefs.getString(KEY_FOCUS_BLOCKS, null))
    )

    private fun encodeLimits(map: Map<String, AppLimit>): String =
        map.entries.joinToString(separator = ";") {
            "${it.key}=${it.value.minutesPerDay},${it.value.expiresAtMillis}"
        }

    private fun decodeLimits(raw: String?): Map<String, AppLimit> {
        if (raw.isNullOrBlank()) return emptyMap()
        return raw.split(';').mapNotNull { entry ->
            val idx = entry.lastIndexOf('=')
            if (idx <= 0) return@mapNotNull null
            val pkg = entry.substring(0, idx)
            // Value is "minutes,expiresAtMillis"; the legacy format stored just "minutes".
            val parts = entry.substring(idx + 1).split(',')
            val minutes = parts[0].toIntOrNull() ?: return@mapNotNull null
            val expiry = parts.getOrNull(1)?.toLongOrNull() ?: AppLimit.NO_EXPIRY
            pkg to AppLimit(minutes, expiry)
        }.toMap()
    }

    private fun encodeBlocks(blocks: List<FocusBlock>): String =
        blocks.joinToString(separator = ";") {
            "${it.id},${it.startMinute},${it.endMinute},${it.daysMask},${if (it.enabled) 1 else 0}"
        }

    private fun decodeBlocks(raw: String?): List<FocusBlock> {
        if (raw.isNullOrBlank()) return emptyList()
        return raw.split(';').mapNotNull { entry ->
            val parts = entry.split(',')
            if (parts.size < 5) return@mapNotNull null
            val id = parts[0].takeIf { it.isNotBlank() } ?: return@mapNotNull null
            val start = parts[1].toIntOrNull() ?: return@mapNotNull null
            val end = parts[2].toIntOrNull() ?: return@mapNotNull null
            val mask = parts[3].toIntOrNull() ?: return@mapNotNull null
            val enabled = parts[4].toIntOrNull() == 1
            FocusBlock(id, start, end, mask, enabled)
        }
    }

    private companion object {
        const val PREFS_NAME = "app_focus_settings"
        const val KEY_PROMPT_PACKAGES = "prompt_packages"
        const val KEY_DISABLED_DEFAULTS = "disabled_defaults"
        const val KEY_LIMITS = "daily_limits"
        const val KEY_FOCUS_BLOCKS = "focus_blocks"
    }
}
