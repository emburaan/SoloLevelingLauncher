package com.sumit.clock.ui.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.clock.R
import com.sumit.clock.util.formatTimer

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        if (state.phase == TimerPhase.Idle) {
            DurationPicker(
                hours = state.configHours,
                minutes = state.configMinutes,
                seconds = state.configSeconds,
                onHoursChange = viewModel::setHours,
                onMinutesChange = viewModel::setMinutes,
                onSecondsChange = viewModel::setSeconds
            )
        } else {
            CountdownDisplay(state = state)
        }

        Spacer(modifier = Modifier.height(8.dp))
        TimerActions(
            state = state,
            onToggle = viewModel::toggle,
            onReset = viewModel::reset
        )
    }
}

@Composable
private fun CountdownDisplay(state: TimerUiState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = formatTimer(state.remainingMs),
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (state.phase == TimerPhase.Done) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.timer_done),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun TimerActions(
    state: TimerUiState,
    onToggle: () -> Unit,
    onReset: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        OutlinedButton(
            onClick = onReset,
            enabled = state.phase != TimerPhase.Idle
        ) {
            Text(stringResource(R.string.timer_action_reset))
        }
        Button(
            onClick = onToggle,
            enabled = state.phase != TimerPhase.Idle || state.configuredMs > 0L
        ) {
            Text(
                stringResource(
                    when (state.phase) {
                        TimerPhase.Idle -> R.string.timer_action_start
                        TimerPhase.Running -> R.string.timer_action_pause
                        TimerPhase.Paused -> R.string.timer_action_resume
                        TimerPhase.Done -> R.string.timer_action_reset
                    }
                )
            )
        }
    }
}

@Composable
private fun DurationPicker(
    hours: Int,
    minutes: Int,
    seconds: Int,
    onHoursChange: (Int) -> Unit,
    onMinutesChange: (Int) -> Unit,
    onSecondsChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NumberStepper(
            label = stringResource(R.string.timer_hours),
            value = hours,
            max = 23,
            onValueChange = onHoursChange
        )
        NumberStepper(
            label = stringResource(R.string.timer_minutes),
            value = minutes,
            max = 59,
            onValueChange = onMinutesChange
        )
        NumberStepper(
            label = stringResource(R.string.timer_seconds),
            value = seconds,
            max = 59,
            onValueChange = onSecondsChange
        )
    }
}

@Composable
private fun NumberStepper(
    label: String,
    value: Int,
    max: Int,
    onValueChange: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = { onValueChange(if (value >= max) 0 else value + 1) },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "%02d".format(value),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        IconButton(
            onClick = { onValueChange(if (value <= 0) max else value - 1) },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
