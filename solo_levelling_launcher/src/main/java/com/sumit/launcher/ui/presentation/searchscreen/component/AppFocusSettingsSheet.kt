package com.sumit.launcher.ui.presentation.searchscreen.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.sumit.launcher.R
import com.sumit.launcher.data.focus.AppFocusEntry
import com.sumit.launcher.ui.model.AppInfo

private val PRESET_MINUTES = listOf(5, 15, 30, 60)
private const val MAX_DAYS = 30

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppFocusSettingsSheet(
    app: AppInfo,
    entry: AppFocusEntry,
    onSave: (requirePrompt: Boolean, dailyLimitMinutes: Int?) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var requirePrompt by remember(app.packageName) { mutableStateOf(entry.requirePrompt) }

    val total = entry.dailyLimitMinutes ?: 0
    var days by remember(app.packageName) {
        mutableIntStateOf((total / 1440).coerceIn(0, MAX_DAYS))
    }
    var minutes by remember(app.packageName) {
        mutableIntStateOf(if (days > 0) 0 else (total % 1440).coerceIn(0, 60))
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
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.spacing_5xl),
                vertical = dimensionResource(R.dimen.spacing_md)
            )
        ) {
            Text(
                text = app.label,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_5xl)))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.app_focus_prompt_title),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        stringResource(R.string.app_focus_prompt_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = requirePrompt,
                    onCheckedChange = { requirePrompt = it }
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_5xl)))

            Text(
                stringResource(R.string.app_focus_daily_limit_title),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                stringResource(R.string.app_focus_daily_limit_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.spacing_md)
                )
            ) {
                FilterChip(
                    selected = days == 0 && minutes == 0,
                    onClick = { days = 0; minutes = 0 },
                    label = { Text(stringResource(R.string.app_focus_chip_off)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                PRESET_MINUTES.forEach { preset ->
                    FilterChip(
                        selected = days == 0 && minutes == preset,
                        onClick = { days = 0; minutes = preset },
                        label = {
                            Text(stringResource(R.string.app_focus_chip_minutes, preset))
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_3xl)))

            Text(
                text = formatLimit(days, minutes),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))

            Slider(
                value = days.toFloat(),
                onValueChange = {
                    val snapped = it.toInt().coerceIn(0, MAX_DAYS)
                    days = snapped
                    if (snapped > 0) minutes = 0
                },
                valueRange = 0f..MAX_DAYS.toFloat(),
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "0 days",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "$MAX_DAYS days",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_3xl)))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.action_cancel))
                }
                TextButton(
                    onClick = {
                        val totalMinutes = days * 1440 + minutes
                        onSave(requirePrompt, totalMinutes.takeIf { it > 0 })
                    }
                ) { Text(stringResource(R.string.action_save)) }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_md)))
        }
    }
}

@Composable
private fun formatLimit(days: Int, minutes: Int): String {
    if (days == 0 && minutes == 0) return stringResource(R.string.app_focus_limit_off)
    val parts = mutableListOf<String>()
    if (days > 0) {
        parts += if (days == 1) stringResource(R.string.app_focus_label_one_day)
        else stringResource(R.string.app_focus_label_days, days)
    }
    if (minutes > 0) {
        parts += if (minutes == 60) stringResource(R.string.app_focus_label_one_hour)
        else stringResource(R.string.app_focus_label_minutes, minutes)
    }
    return parts.joinToString(" ")
}
