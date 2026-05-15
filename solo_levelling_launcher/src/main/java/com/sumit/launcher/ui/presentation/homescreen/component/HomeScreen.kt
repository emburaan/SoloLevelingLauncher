package com.sumit.launcher.ui.presentation.homescreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalLifecycleOwner
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
    DisposableEffect(lifecycleOwner) {
        var tickJob: Job? = null
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    tickJob?.cancel()
                    tickJob = scope.launch {
                        while (isActive) {
                            usageViewModel.refresh()
                            delay(60_000)
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
                .padding(top = 60.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top row: chart on the left, clock on the right (16.dp from edge)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                when (val state = usageState) {
                    is UsageUiState.Ready -> UsageBarChart(usageData = state.days)
                    UsageUiState.NeedsPermission -> UsageAccessPrompt(
                        modifier = Modifier.width(160.dp)
                    )
                    UsageUiState.Loading -> Spacer(modifier = Modifier.size(160.dp, 180.dp))
                }
                NeumorphicAnalogClock(modifier = Modifier.size(120.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
            TaskListSection(
                onMaxReached = {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(motivationalMessages.random())
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                AchievementTile(title = "Physical")
                AchievementTile(title = "Mental")
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                AchievementTile(title = "Spiritual")
                AchievementTile(title = "Accountability")
            }
            Spacer(modifier = Modifier.height(30.dp))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
        ) { data ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .neumorphicSurface(
                        shape = RoundedCornerShape(22.dp),
                        elevation = 14.dp,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Text(
                    text = data.visuals.message,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
