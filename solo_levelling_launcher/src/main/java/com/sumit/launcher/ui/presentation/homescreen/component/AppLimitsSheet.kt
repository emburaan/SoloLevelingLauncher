package com.sumit.launcher.ui.presentation.homescreen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.launcher.R
import com.sumit.launcher.data.focus.AppFocusEntry
import com.sumit.launcher.ui.model.AppInfo
import com.sumit.launcher.ui.presentation.homescreen.AppLimitsViewModel
import com.sumit.launcher.ui.presentation.searchscreen.component.AppFocusSettingsSheet
import com.sumit.launcher.ui.theme.neumorphicSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLimitsSheet(
    onDismiss: () -> Unit,
    viewModel: AppLimitsViewModel = hiltViewModel()
) {
    val limitedApps by viewModel.limitedApps.collectAsState()
    val focusState by viewModel.focusState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var editingApp by remember { mutableStateOf<AppInfo?>(null) }

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
                text = stringResource(R.string.app_limits_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.app_limits_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_3xl)))

            if (limitedApps.isEmpty()) {
                Text(
                    text = stringResource(R.string.app_limits_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                limitedApps.forEach { app ->
                    LimitedAppRow(
                        app = app,
                        entry = focusState.entryFor(app.packageName),
                        onClick = { editingApp = app }
                    )
                }
            }

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

    editingApp?.let { app ->
        AppFocusSettingsSheet(
            app = app,
            entry = focusState.entryFor(app.packageName),
            onSave = { requirePrompt, dailyLimitMinutes, days ->
                viewModel.setRequirePrompt(app.packageName, requirePrompt)
                viewModel.setDailyLimit(app.packageName, dailyLimitMinutes, days)
                editingApp = null
            },
            onDismiss = { editingApp = null }
        )
    }
}

@Composable
private fun LimitedAppRow(
    app: AppInfo,
    entry: AppFocusEntry,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = dimensionResource(R.dimen.spacing_sm)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dimensionResource(R.dimen.app_icon_container))
                .neumorphicSurface(
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_chip)),
                    elevation = dimensionResource(R.dimen.elevation_card_sm),
                    color = MaterialTheme.colorScheme.surfaceContainer
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = app.icon,
                contentDescription = app.label,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.app_icon))
                    .padding(2.dp)
            )
        }
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_2xl)))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = app.label,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
            Text(
                text = limitSummary(entry),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun limitSummary(entry: AppFocusEntry): String {
    val minutes = entry.dailyLimitMinutes ?: return ""
    val allowance = stringResource(R.string.app_focus_limit_per_day, minutes)
    val days = entry.limitDaysRemaining
    val duration = when {
        days == null -> stringResource(R.string.app_focus_duration_ongoing)
        days <= 1 -> stringResource(R.string.app_focus_duration_one_day)
        else -> stringResource(R.string.app_focus_duration_days, days)
    }
    return "$allowance · $duration"
}
