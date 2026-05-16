package com.sumit.launcher.ui.presentation.homescreen.component

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.sumit.launcher.R
import com.sumit.launcher.ui.presentation.homescreen.UsageUiState
import com.sumit.launcher.ui.presentation.homescreen.UsageViewModel
import com.sumit.sololevelinglauncher.ui.theme.neumorphicSurface
import com.sumit.todo_list.presentation.component.TaskListSection
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    usageViewModel: UsageViewModel = hiltViewModel()
) {
    val usageState by usageViewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val motivationalMessages = stringArrayResource(R.array.task_limit_motivational_messages)
    var showFocusBlocks by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    val context = LocalContext.current
    DisposableEffect(lifecycleOwner) {
        var tickJob: Job? = null
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    tickJob?.cancel()
                    tickJob = scope.launch {
                        while (isActive) {
                            usageViewModel.refresh()
                            // Sleep until the next minute mark OR just past midnight,
                            // whichever comes first. This snaps the pickup/screen-time
                            // bar to 0 exactly at the day boundary instead of up to
                            // 60 s later.
                            delay(delayToNextTickMs())
                        }
                    }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    tickJob?.cancel()
                    tickJob = null
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            tickJob?.cancel()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = dimensionResource(R.dimen.spacing_screen_top),
                    bottom = dimensionResource(R.dimen.spacing_3xl)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FocusModeIndicator()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.spacing_3xl)),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.spacing_3xl)
                )
            ) {
                when (val state = usageState) {
                    is UsageUiState.Ready -> UsageBarChart(
                        usageData = state.days,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showFocusBlocks = true }
                    )
                    UsageUiState.NeedsPermission -> UsageAccessPrompt(
                        modifier = Modifier.weight(1f)
                    )
                    UsageUiState.Loading -> Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .height(180.dp)
                    )
                }
                NeumorphicAnalogClock(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clickable { openClock(context) }
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_6xl)))
            TaskListSection(
                onMaxReached = {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(motivationalMessages.random())
                    }
                }
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(R.dimen.spacing_3xl),
                        end = dimensionResource(R.dimen.spacing_3xl)
                    ),
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.spacing_md)
                ),
                verticalAlignment = Alignment.Top
            ) {
                DaysLeftCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(dimensionResource(R.dimen.card_height_date))
                )
                DateCard(
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.card_width_date))
                        .height(dimensionResource(R.dimen.card_height_date))
                        .clickable { openCalendar(context) }
                )
                Column(
                    modifier = Modifier.width(IntrinsicSize.Max),
                    verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.spacing_md)
                    )
                ) {
                        val cardModifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.card_height_compact))
                        SettingsCard(
                            onClick = { showSettings = true },
                            modifier = cardModifier
                        )
                        (usageState as? UsageUiState.Ready)?.let { ready ->
                            PickupCounter(
                                stats = ready.today,
                                modifier = cardModifier
                            )
                        }
                    }
                }

            Spacer(modifier = Modifier.weight(1f))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = dimensionResource(R.dimen.snackbar_bottom),
                    start = dimensionResource(R.dimen.spacing_3xl),
                    end = dimensionResource(R.dimen.spacing_3xl)
                )
        ) { data ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .neumorphicSurface(
                        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_card_xl)),
                        elevation = dimensionResource(R.dimen.elevation_card_lg),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                    .padding(
                        horizontal = dimensionResource(R.dimen.card_padding_h),
                        vertical = dimensionResource(R.dimen.card_padding_v_lg)
                    )
            ) {
                Text(
                    text = data.visuals.message,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    if (showFocusBlocks) {
        FocusBlocksSheet(onDismiss = { showFocusBlocks = false })
    }

    if (showSettings) {
        SettingsSheet(
            onDismiss = { showSettings = false },
            onOpenFocusBlocks = { showFocusBlocks = true }
        )
    }
}

private fun openClock(context: Context) {
    val intent = Intent(AlarmClock.ACTION_SHOW_ALARMS)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    runCatching { context.startActivity(intent) }
}

private fun openCalendar(context: Context) {
    val intent = Intent(Intent.ACTION_MAIN)
        .addCategory(Intent.CATEGORY_APP_CALENDAR)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    runCatching { context.startActivity(intent) }
}

/**
 * Returns how long to sleep before the next [UsageViewModel.refresh]. Caps at 60 s,
 * but if the next local midnight comes sooner, sleep until just after midnight so
 * the pickup count snaps to 0 cleanly instead of lagging by up to a minute.
 */
private fun delayToNextTickMs(): Long {
    val now = java.util.Calendar.getInstance()
    val midnight = (now.clone() as java.util.Calendar).apply {
        add(java.util.Calendar.DAY_OF_YEAR, 1)
        set(java.util.Calendar.HOUR_OF_DAY, 0)
        set(java.util.Calendar.MINUTE, 0)
        set(java.util.Calendar.SECOND, 0)
        set(java.util.Calendar.MILLISECOND, 0)
    }
    val msToMidnight = midnight.timeInMillis - now.timeInMillis + 200L
    return minOf(60_000L, msToMidnight)
}
