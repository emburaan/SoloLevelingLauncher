package com.sumit.clock.ui.alarm

import androidx.lifecycle.ViewModel
import com.sumit.clock.alarm.Alarm
import com.sumit.clock.alarm.AlarmRepository
import com.sumit.clock.alarm.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val repository: AlarmRepository,
    private val scheduler: AlarmScheduler
) : ViewModel() {

    val alarms: StateFlow<List<Alarm>> = repository.alarms

    fun canScheduleExact(): Boolean = scheduler.canScheduleExact()

    fun setEnabled(alarm: Alarm, enabled: Boolean) {
        val saved = repository.upsert(alarm.copy(enabled = enabled))
        if (enabled) scheduler.schedule(saved) else scheduler.cancel(saved.id)
    }

    fun save(alarm: Alarm) {
        val saved = repository.upsert(alarm)
        if (saved.enabled) scheduler.schedule(saved) else scheduler.cancel(saved.id)
    }

    fun delete(alarm: Alarm) {
        scheduler.cancel(alarm.id)
        repository.delete(alarm.id)
    }
}
