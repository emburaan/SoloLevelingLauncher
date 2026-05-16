package com.sumit.launcher.data.focus

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class AppFocusEntry(
    val requirePrompt: Boolean = false,
    val dailyLimitMinutes: Int? = null
)

data class AppFocusState(
    /** Apps the user has explicitly turned on. */
    val promptPackages: Set<String> = emptySet(),
    /** Default distractors the user has explicitly turned off. */
    val disabledDefaults: Set<String> = emptySet(),
    val dailyLimits: Map<String, Int> = emptyMap(),
    val focusBlocks: List<FocusBlock> = emptyList()
) {
    fun entryFor(packageName: String): AppFocusEntry {
        val isDefault = packageName in DEFAULT_DISTRACTOR_PACKAGES
        val effective = packageName in promptPackages ||
            (isDefault && packageName !in disabledDefaults)
        return AppFocusEntry(
            requirePrompt = effective,
            dailyLimitMinutes = dailyLimits[packageName]
        )
    }

    fun activeFocusBlock(at: Long = System.currentTimeMillis()): FocusBlock? =
        focusBlocks.firstOrNull { it.isActive(at) }
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

    /** Pass null to clear the daily limit for [packageName]. */
    fun setDailyLimit(packageName: String, minutes: Int?) {
        val current = _state.value.dailyLimits
        val updated = if (minutes == null || minutes <= 0) {
            current - packageName
        } else {
            current + (packageName to minutes)
        }
        prefs.edit().putString(KEY_LIMITS, encodeLimits(updated)).apply()
        _state.value = _state.value.copy(dailyLimits = updated)
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

    private fun encodeLimits(map: Map<String, Int>): String =
        map.entries.joinToString(separator = ";") { "${it.key}=${it.value}" }

    private fun decodeLimits(raw: String?): Map<String, Int> {
        if (raw.isNullOrBlank()) return emptyMap()
        return raw.split(';').mapNotNull { entry ->
            val idx = entry.lastIndexOf('=')
            if (idx <= 0) return@mapNotNull null
            val pkg = entry.substring(0, idx)
            val minutes = entry.substring(idx + 1).toIntOrNull() ?: return@mapNotNull null
            pkg to minutes
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
