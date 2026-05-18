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
            ACTION_SNOOZE -> handleSnooze(intent)
        }
    }

    private fun handleFire(context: Context, intent: Intent) {
        val alarmId = intent.getLongExtra(EXTRA_ALARM_ID, -1L)
        if (alarmId < 0) return
        val alarm = repository.find(alarmId) ?: return

        val ringIntent = Intent(context, AlarmRingActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NO_HISTORY
            )
            putExtra(EXTRA_ALARM_ID, alarmId)
            putExtra(EXTRA_ALARM_LABEL, alarm.label)
            putExtra(EXTRA_ALARM_HOUR, alarm.hour)
            putExtra(EXTRA_ALARM_MINUTE, alarm.minute)
            putExtra(EXTRA_MATH_PROBLEMS, alarm.mathProblems)
            putExtra(EXTRA_MATH_DIFFICULTY, alarm.mathDifficulty.name)
            putExtra(EXTRA_SHAKE_COUNT, alarm.shakeCount)
            putExtra(EXTRA_TYPING_PHRASE, alarm.typingPhrase)
        }
        runCatching { context.startActivity(ringIntent) }

        if (alarm.repeatDaily) {
            scheduler.schedule(alarm)
        } else {
            repository.upsert(alarm.copy(enabled = false))
        }
    }

    private fun handleSnooze(intent: Intent) {
        val alarmId = intent.getLongExtra(EXTRA_ALARM_ID, -1L)
        if (alarmId < 0) return
        val alarm = repository.find(alarmId) ?: return
        scheduler.schedule(alarm, System.currentTimeMillis() + SNOOZE_MS)
    }

    companion object {
        const val ACTION_FIRE = "com.sumit.clock.action.ALARM_FIRE"
        const val ACTION_SNOOZE = "com.sumit.clock.action.ALARM_SNOOZE"
        const val EXTRA_ALARM_ID = "alarm_id"
        const val EXTRA_ALARM_LABEL = "alarm_label"
        const val EXTRA_ALARM_HOUR = "alarm_hour"
        const val EXTRA_ALARM_MINUTE = "alarm_minute"
        const val EXTRA_MATH_PROBLEMS = "math_problems"
        const val EXTRA_MATH_DIFFICULTY = "math_difficulty"
        const val EXTRA_SHAKE_COUNT = "shake_count"
        const val EXTRA_TYPING_PHRASE = "typing_phrase"
        const val SNOOZE_MS = 5L * 60L * 1000L
    }
}
