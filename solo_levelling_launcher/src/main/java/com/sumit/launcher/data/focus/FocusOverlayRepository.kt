package com.sumit.launcher.data.focus

import android.content.Context
import com.sumit.launcher.service.FocusCheckService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Tracks whether the persistent mid-app focus check overlay is enabled. The
 * caller is responsible for verifying that overlay permission is granted before
 * calling [setEnabled] with true.
 */
@Singleton
class FocusOverlayRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val _enabled = MutableStateFlow(prefs.getBoolean(KEY_ENABLED, false))
    val enabled: StateFlow<Boolean> = _enabled.asStateFlow()

    fun setEnabled(value: Boolean) {
        prefs.edit().putBoolean(KEY_ENABLED, value).apply()
        _enabled.value = value
        if (value) {
            FocusCheckService.start(context)
        } else {
            FocusCheckService.stop(context)
        }
    }

    companion object {
        const val PREFS = "focus_overlay"
        const val KEY_ENABLED = "enabled"
    }
}
