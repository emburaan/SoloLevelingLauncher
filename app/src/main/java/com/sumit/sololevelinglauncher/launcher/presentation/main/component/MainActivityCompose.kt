package com.sumit.sololevelinglauncher.launcher.presentation.main.component

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.launcher.ui.presentation.homescreen.UsageUiState
import com.sumit.launcher.ui.presentation.homescreen.UsageViewModel
import com.sumit.launcher.ui.presentation.homescreen.component.HomeScreen
import com.sumit.launcher.ui.presentation.searchscreen.SearchScreenViewModel
import com.sumit.launcher.ui.presentation.searchscreen.component.AppListWithSearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityCompose() {
    val searchScreenViewModel: SearchScreenViewModel = hiltViewModel()
    val usageViewModel: UsageViewModel = hiltViewModel()
    val apps by searchScreenViewModel.apps.collectAsState()
    val usageState by usageViewModel.state.collectAsState()
    val pagerState = rememberPagerState(0, pageCount = { 2 })
    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(true) }
    var dismissedUsageDialog by rememberSaveable { mutableStateOf(false) }

    if (!showSheet
        && !dismissedUsageDialog
        && usageState is UsageUiState.NeedsPermission
    ) {
        AlertDialog(
            onDismissRequest = { dismissedUsageDialog = true },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(24.dp),
            title = { Text("Usage access") },
            text = {
                Text(
                    "Solo Leveling Launcher uses your screen-time history to show the " +
                        "weekly usage chart on the home screen. Grant access in Settings?"
                )
            },
            confirmButton = {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        dismissedUsageDialog = true
                    }
                ) { Text("Open Settings") }
            },
            dismissButton = {
                TextButton(onClick = { dismissedUsageDialog = true }) { Text("Not now") }
            }
        )
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                Text(
                    "Set as Default Launcher",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "To get the best experience, set this app as your default launcher.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_MAIN)
                            .addCategory(Intent.CATEGORY_HOME)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                        showSheet = false
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 12.dp)
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Set as Default Launcher")
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> HomeScreen()
                1 -> AppListWithSearchScreen(apps)
            }
        }
    }
}
