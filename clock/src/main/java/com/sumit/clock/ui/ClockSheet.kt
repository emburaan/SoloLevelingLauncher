package com.sumit.clock.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sumit.clock.R
import com.sumit.clock.ui.alarm.AlarmScreen
import com.sumit.clock.ui.stopwatch.StopwatchScreen
import com.sumit.clock.ui.timer.TimerScreen
import com.sumit.clock.ui.worldclock.WorldClockScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClockSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTab by remember { mutableStateOf(ClockTab.Stopwatch) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.clock_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 8.dp)
            )
            TabRow(
                selectedTabIndex = selectedTab.ordinal,
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                ClockTab.entries.forEach { tab ->
                    Tab(
                        selected = tab == selectedTab,
                        onClick = { selectedTab = tab },
                        text = { Text(stringResource(tab.labelRes)) }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 360.dp)
            ) {
                when (selectedTab) {
                    ClockTab.Alarm -> AlarmScreen()
                    ClockTab.Clock -> WorldClockScreen()
                    ClockTab.Timer -> TimerScreen()
                    ClockTab.Stopwatch -> StopwatchScreen()
                }
            }
        }
    }
}

private enum class ClockTab(val labelRes: Int) {
    Alarm(R.string.clock_tab_alarm),
    Clock(R.string.clock_tab_clock),
    Timer(R.string.clock_tab_timer),
    Stopwatch(R.string.clock_tab_stopwatch)
}
