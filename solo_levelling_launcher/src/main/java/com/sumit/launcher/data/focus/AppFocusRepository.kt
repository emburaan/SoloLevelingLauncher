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
    val promptPackages: Set<String> = emptySet(),
    val dailyLimits: Map<String, Int> = emptyMap()
) {
    fun entryFor(packageName: String): AppFocusEntry = AppFocusEntry(
        requirePrompt = packageName in promptPackages,
        dailyLimitMinutes = dailyLimits[packageName]
    )
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
        val current = _state.value.promptPackages
        val updated = if (enabled) current + packageName else current - packageName
        prefs.edit().putStringSet(KEY_PROMPT_PACKAGES, updated).apply()
        _state.value = _state.value.copy(promptPackages = updated)
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

    private fun load(): AppFocusState = AppFocusState(
        promptPackages = prefs.getStringSet(KEY_PROMPT_PACKAGES, emptySet())
            ?.toSet().orEmpty(),
        dailyLimits = decodeLimits(prefs.getString(KEY_LIMITS, null))
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

    private companion object {
        const val PREFS_NAME = "app_focus_settings"
        const val KEY_PROMPT_PACKAGES = "prompt_packages"
        const val KEY_LIMITS = "daily_limits"
    }
}
