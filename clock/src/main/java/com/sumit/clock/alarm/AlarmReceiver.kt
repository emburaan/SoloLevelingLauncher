package com.sumit.clock.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject lateinit var repository: AlarmRepository
    @Inject lateinit var scheduler: AlarmScheduler

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_FIRE -> handleFire(context, intent)
        }
    }

    private fun handleFire(context: Context, intent: Intent) {
        AlarmWakeLock.acquire(context)
        val alarmId = intent.getLongExtra(EXTRA_ALARM_ID, -1L)
        if (alarmId < 0) {
            AlarmWakeLock.release()
            return
        }
        val alarm = repository.find(alarmId)
        if (alarm == null) {
            AlarmWakeLock.release()
            return
        }

        val fireIntent = Intent().apply {
            putExtra(EXTRA_ALARM_ID, alarmId)
            putExtra(EXTRA_ALARM_LABEL, alarm.label)
            putExtra(EXTRA_ALARM_HOUR, alarm.hour)
            putExtra(EXTRA_ALARM_MINUTE, alarm.minute)
            putExtra(EXTRA_MATH_PROBLEMS, alarm.mathProblems)
            putExtra(EXTRA_MATH_DIFFICULTY, alarm.mathDifficulty.name)
            putExtra(EXTRA_SHAKE_COUNT, alarm.shakeCount)
            putExtra(EXTRA_TYPING_PHRASE, alarm.typingPhrase)
        }
        AlarmRingService.start(context, fireIntent)

        if (alarm.repeatDaily) {
            scheduler.schedule(alarm)
        } else {
            repository.upsert(alarm.copy(enabled = false))
        }
    }

    companion object {
        const val ACTION_FIRE = "com.sumit.clock.action.ALARM_FIRE"
        const val EXTRA_ALARM_ID = "alarm_id"
        const val EXTRA_ALARM_LABEL = "alarm_label"
        const val EXTRA_ALARM_HOUR = "alarm_hour"
        const val EXTRA_ALARM_MINUTE = "alarm_minute"
        const val EXTRA_MATH_PROBLEMS = "math_problems"
        const val EXTRA_MATH_DIFFICULTY = "math_difficulty"
        const val EXTRA_SHAKE_COUNT = "shake_count"
        const val EXTRA_TYPING_PHRASE = "typing_phrase"
    }
}
