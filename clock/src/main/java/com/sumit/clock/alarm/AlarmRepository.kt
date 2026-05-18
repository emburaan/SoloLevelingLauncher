package com.sumit.clock.alarm

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val _alarms = MutableStateFlow(load())
    val alarms: StateFlow<List<Alarm>> = _alarms.asStateFlow()

    fun all(): List<Alarm> = _alarms.value

    fun find(id: Long): Alarm? = _alarms.value.firstOrNull { it.id == id }

    fun upsert(alarm: Alarm): Alarm {
        val withId = if (alarm.id == Alarm.NEW_ID) alarm.copy(id = nextId()) else alarm
        val next = _alarms.value.filterNot { it.id == withId.id } + withId
        save(next)
        return withId
    }

    fun delete(id: Long) {
        val next = _alarms.value.filterNot { it.id == id }
        if (next.size == _alarms.value.size) return
        save(next)
    }

    private fun nextId(): Long {
        val v = prefs.getLong(KEY_NEXT_ID, 1L)
        prefs.edit().putLong(KEY_NEXT_ID, v + 1L).apply()
        return v
    }

    private fun save(list: List<Alarm>) {
        val arr = JSONArray()
        list.forEach { a ->
            arr.put(
                JSONObject().apply {
                    put("id", a.id)
                    put("hour", a.hour)
                    put("minute", a.minute)
                    put("label", a.label)
                    put("enabled", a.enabled)
                    put("repeatDaily", a.repeatDaily)
                    put("mathProblems", a.mathProblems)
                    put("mathDifficulty", a.mathDifficulty.name)
                    put("shakeCount", a.shakeCount)
                    put("typingPhrase", a.typingPhrase)
                }
            )
        }
        prefs.edit().putString(KEY_ALARMS, arr.toString()).apply()
        _alarms.value = list.sortedWith(compareBy({ it.hour }, { it.minute }))
    }

    private fun load(): List<Alarm> {
        val raw = prefs.getString(KEY_ALARMS, null) ?: return emptyList()
        val arr = runCatching { JSONArray(raw) }.getOrNull() ?: return emptyList()
        return List(arr.length()) { i ->
            val o = arr.getJSONObject(i)
            Alarm(
                id = o.getLong("id"),
                hour = o.getInt("hour"),
                minute = o.getInt("minute"),
                label = o.optString("label", ""),
                enabled = o.getBoolean("enabled"),
                repeatDaily = o.optBoolean("repeatDaily", false),
                mathProblems = o.optInt("mathProblems", DismissDefaults.MATH_PROBLEMS),
                mathDifficulty = runCatching {
                    MathDifficulty.valueOf(
                        o.optString("mathDifficulty", DismissDefaults.MATH_DIFFICULTY.name)
                    )
                }.getOrDefault(DismissDefaults.MATH_DIFFICULTY),
                shakeCount = o.optInt("shakeCount", DismissDefaults.SHAKE_COUNT),
                typingPhrase = o.optString("typingPhrase", DismissDefaults.TYPING_PHRASE)
            )
        }.sortedWith(compareBy({ it.hour }, { it.minute }))
    }

    private companion object {
        const val PREFS = "alarms"
        const val KEY_ALARMS = "alarms"
        const val KEY_NEXT_ID = "next_id"
    }
}
