package com.sumit.sololevelinglauncher.launcher.presentation.main.component

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.launcher.ui.presentation.homescreen.UsageUiState
import com.sumit.launcher.ui.presentation.homescreen.UsageViewModel
import com.sumit.launcher.ui.presentation.homescreen.component.HomeScreen
import com.sumit.launcher.ui.presentation.searchscreen.component.AppListWithSearchScreen
import com.sumit.sololevelinglauncher.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityCompose() {
    val usageViewModel: UsageViewModel = hiltViewModel()
    val usageState by usageViewModel.state.collectAsState()
    val pagerState = rememberPagerState(0, pageCount = { 2 })
    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(!isDefaultLauncher(context)) }
    var dismissedUsageDialog by rememberSaveable { mutableStateOf(false) }
    var dismissedBatteryDialog by rememberSaveable { mutableStateOf(false) }

    val roleLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { showSheet = !isDefaultLauncher(context) }

    BackHandler(enabled = true) { /* Back is disabled on the launcher screens. */ }

    if (!showSheet
        && !dismissedUsageDialog
        && usageState is UsageUiState.NeedsPermission
    ) {
        AlertDialog(
            onDismissRequest = { dismissedUsageDialog = true },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(dimensionResource(R.dimen.dialog_corner)),
            title = { Text(stringResource(R.string.dialog_usage_access_title)) },
            text = { Text(stringResource(R.string.dialog_usage_access_body)) },
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
                ) { Text(stringResource(R.string.dialog_open_settings)) }
            },
            dismissButton = {
                TextButton(onClick = { dismissedUsageDialog = true }) {
                    Text(stringResource(R.string.dialog_not_now))
                }
            }
        )
    }

    if (!showSheet
        && dismissedUsageDialog
        && !dismissedBatteryDialog
        && !isIgnoringBatteryOptimizations(context)
    ) {
        AlertDialog(
            onDismissRequest = { dismissedBatteryDialog = true },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(dimensionResource(R.dimen.dialog_corner)),
            title = { Text(stringResource(R.string.dialog_battery_title)) },
            text = { Text(stringResource(R.string.dialog_battery_body)) },
            confirmButton = {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                                .setData(Uri.parse("package:${context.packageName}"))
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        dismissedBatteryDialog = true
                    }
                ) { Text(stringResource(R.string.dialog_battery_allow)) }
            },
            dismissButton = {
                TextButton(onClick = { dismissedBatteryDialog = true }) {
                    Text(stringResource(R.string.dialog_not_now))
                }
            }
        )
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(
                topStart = dimensionResource(R.dimen.sheet_corner),
                topEnd = dimensionResource(R.dimen.sheet_corner)
            )
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.sheet_padding_h),
                    vertical = dimensionResource(R.dimen.sheet_padding_v)
                )
            ) {
                Text(
                    stringResource(R.string.default_launcher_sheet_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    stringResource(R.string.default_launcher_sheet_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.sheet_body_top))
                )
                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val roleManager = context.getSystemService(RoleManager::class.java)
                            if (roleManager != null
                                && roleManager.isRoleAvailable(RoleManager.ROLE_HOME)
                                && !roleManager.isRoleHeld(RoleManager.ROLE_HOME)
                            ) {
                                roleLauncher.launch(
                                    roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
                                )
                                return@Button
                            }
                        }
                        context.startActivity(
                            Intent(Settings.ACTION_HOME_SETTINGS)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        showSheet = false
                    },
                    shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner)),
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(R.dimen.sheet_button_top),
                            bottom = dimensionResource(R.dimen.sheet_button_bottom)
                        )
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.sheet_button_height))
                ) {
                    Text(stringResource(R.string.default_launcher_sheet_button))
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
                1 -> AppListWithSearchScreen()
            }
        }
    }
}

private fun isDefaultLauncher(context: Context): Boolean {
    val resolveInfo = context.packageManager.resolveActivity(
        Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME),
        PackageManager.MATCH_DEFAULT_ONLY
    )
    return resolveInfo?.activityInfo?.packageName == context.packageName
}

private fun isIgnoringBatteryOptimizations(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
        ?: return true
    return powerManager.isIgnoringBatteryOptimizations(context.packageName)
}
