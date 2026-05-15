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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.sumit.launcher.ui.presentation.homescreen.UsageUiState
import com.sumit.launcher.ui.presentation.homescreen.UsageViewModel
import com.sumit.todo_list.presentation.component.TaskListSection

@Composable
fun HomeScreen(
    usageViewModel: UsageViewModel = hiltViewModel()
) {
    val usageState by usageViewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) usageViewModel.refresh()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
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

            Spacer(modifier = Modifier.weight(1f))

            // Bottom block: tasks + achievements
            TaskListSection()
            Spacer(modifier = Modifier.height(20.dp))
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
    }
}
