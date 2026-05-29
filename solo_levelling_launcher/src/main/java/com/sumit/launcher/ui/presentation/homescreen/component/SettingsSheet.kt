package com.sumit.launcher.ui.presentation.homescreen.component

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.launcher.R
import com.sumit.launcher.ui.presentation.homescreen.SettingsSheetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    onDismiss: () -> Unit,
    onOpenFocusBlocks: () -> Unit,
    viewModel: SettingsSheetViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState()
    var showAdultBlocker by remember { mutableStateOf(false) }
    val overlayEnabled by viewModel.overlayEnabled.collectAsState()
    val context = LocalContext.current

    val overlayPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Settings.canDrawOverlays(context)) viewModel.setOverlayEnabled(true)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(
            topStart = dimensionResource(R.dimen.corner_sheet),
            topEnd = dimensionResource(R.dimen.corner_sheet)
        )
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = dimensionResource(R.dimen.spacing_5xl),
                    vertical = dimensionResource(R.dimen.spacing_md)
                )
        ) {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_3xl)))

            SettingsRow(
                title = stringResource(R.string.settings_focus_schedule_title),
                subtitle = stringResource(R.string.settings_focus_schedule_subtitle),
                onClick = {
                    onDismiss()
                    onOpenFocusBlocks()
                }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            SettingsRow(
                title = stringResource(R.string.settings_adult_blocker_title),
                subtitle = stringResource(R.string.settings_adult_blocker_subtitle),
                onClick = { showAdultBlocker = true }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            SettingsToggleRow(
                title = stringResource(R.string.settings_overlay_title),
                subtitle = stringResource(R.string.settings_overlay_subtitle),
                checked = overlayEnabled,
                onCheckedChange = { wantEnabled ->
                    if (wantEnabled) {
                        if (Settings.canDrawOverlays(context)) {
                            viewModel.setOverlayEnabled(true)
                        } else {
                            overlayPermissionLauncher.launch(
                                Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:${context.packageName}")
                                )
                            )
                        }
                    } else {
                        viewModel.setOverlayEnabled(false)
                    }
                }
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.action_close))
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_md)))
        }
    }

    if (showAdultBlocker) {
        AdultBlockerDialog(
            onDismiss = { showAdultBlocker = false },
            onOpenSettings = {
                showAdultBlocker = false
                onDismiss()
            }
        )
    }
}

@Composable
private fun SettingsRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = dimensionResource(R.dimen.spacing_xl))
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.spacing_xl)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
