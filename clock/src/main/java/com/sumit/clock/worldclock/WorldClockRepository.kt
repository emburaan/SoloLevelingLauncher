package com.sumit.clock.worldclock

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorldClockRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val _zoneIds = MutableStateFlow(loadOrdered())
    val zoneIds: StateFlow<List<String>> = _zoneIds.asStateFlow()

    fun add(zoneId: String) {
        val current = _zoneIds.value
        if (current.contains(zoneId)) return
        save(current + zoneId)
    }

    fun remove(zoneId: String) {
        val next = _zoneIds.value.filterNot { it == zoneId }
        if (next.size == _zoneIds.value.size) return
        save(next)
    }

    private fun save(list: List<String>) {
        prefs.edit().putString(KEY_ZONES, list.joinToString(SEP)).apply()
        _zoneIds.value = list
    }

    private fun loadOrdered(): List<String> {
        val raw = prefs.getString(KEY_ZONES, null).orEmpty()
        return if (raw.isEmpty()) emptyList() else raw.split(SEP)
    }

    private companion object {
        const val PREFS = "world_clock"
        const val KEY_ZONES = "zone_ids"
        const val SEP = "|"
    }
}
