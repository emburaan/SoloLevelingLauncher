package com.sumit.clock.ui.stopwatch

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

data class StopwatchUiState(
    val elapsedMs: Long = 0L,
    val running: Boolean = false,
    val laps: List<Long> = emptyList()
)

@HiltViewModel
class StopwatchViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(StopwatchUiState())
    val state: StateFlow<StopwatchUiState> = _state.asStateFlow()

    private var startElapsedMs: Long = 0L
    private var accumulatedMs: Long = 0L
    private var tickJob: Job? = null

    fun toggle() {
        if (_state.value.running) pause() else start()
    }

    fun reset() {
        tickJob?.cancel()
        tickJob = null
        accumulatedMs = 0L
        startElapsedMs = 0L
        _state.value = StopwatchUiState()
    }

    fun lap() {
        if (!_state.value.running) return
        _state.update { it.copy(laps = listOf(it.elapsedMs) + it.laps) }
    }

    private fun start() {
        startElapsedMs = SystemClock.elapsedRealtime()
        _state.update { it.copy(running = true) }
        tickJob = viewModelScope.launch {
            while (isActive) {
                val now = SystemClock.elapsedRealtime()
                _state.update { it.copy(elapsedMs = accumulatedMs + (now - startElapsedMs)) }
                delay(33L)
            }
        }
    }

    private fun pause() {
        tickJob?.cancel()
        tickJob = null
        val now = SystemClock.elapsedRealtime()
        accumulatedMs += now - startElapsedMs
        _state.update { it.copy(running = false, elapsedMs = accumulatedMs) }
    }
}
