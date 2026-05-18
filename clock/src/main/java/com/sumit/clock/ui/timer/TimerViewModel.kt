package com.sumit.clock.ui.timer

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TimerPhase { Idle, Running, Paused, Done }

data class TimerUiState(
    val phase: TimerPhase = TimerPhase.Idle,
    val configHours: Int = 0,
    val configMinutes: Int = 5,
    val configSeconds: Int = 0,
    val remainingMs: Long = 0L
) {
    val configuredMs: Long
        get() = ((configHours * 3600L) + (configMinutes * 60L) + configSeconds) * 1000L
}

@HiltViewModel
class TimerViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(TimerUiState())
    val state: StateFlow<TimerUiState> = _state.asStateFlow()

    private var deadlineElapsedMs: Long = 0L
    private var tickJob: Job? = null

    fun setHours(h: Int) = _state.update { it.copy(configHours = h.coerceIn(0, 23)) }
    fun setMinutes(m: Int) = _state.update { it.copy(configMinutes = m.coerceIn(0, 59)) }
    fun setSeconds(s: Int) = _state.update { it.copy(configSeconds = s.coerceIn(0, 59)) }

    fun toggle() {
        when (_state.value.phase) {
            TimerPhase.Idle -> start()
            TimerPhase.Running -> pause()
            TimerPhase.Paused -> resume()
            TimerPhase.Done -> reset()
        }
    }

    fun reset() {
        tickJob?.cancel()
        tickJob = null
        _state.update { it.copy(phase = TimerPhase.Idle, remainingMs = 0L) }
    }

    private fun start() {
        val total = _state.value.configuredMs
        if (total <= 0L) return
        deadlineElapsedMs = SystemClock.elapsedRealtime() + total
        _state.update { it.copy(phase = TimerPhase.Running, remainingMs = total) }
        launchTick()
    }

    private fun pause() {
        tickJob?.cancel()
        tickJob = null
        _state.update { it.copy(phase = TimerPhase.Paused) }
    }

    private fun resume() {
        val remaining = _state.value.remainingMs
        if (remaining <= 0L) return
        deadlineElapsedMs = SystemClock.elapsedRealtime() + remaining
        _state.update { it.copy(phase = TimerPhase.Running) }
        launchTick()
    }

    private fun launchTick() {
        tickJob = viewModelScope.launch {
            while (isActive) {
                val remaining = (deadlineElapsedMs - SystemClock.elapsedRealtime()).coerceAtLeast(0L)
                _state.update { it.copy(remainingMs = remaining) }
                if (remaining == 0L) {
                    _state.update { it.copy(phase = TimerPhase.Done) }
                    break
                }
                delay(100L)
            }
            tickJob = null
        }
    }
}
