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
import androidx.compose.ui.unit.dp
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
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            Text(
                text = app.label,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Focus prompt",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Add a 5-second pause before this app opens.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = requirePrompt,
                    onCheckedChange = { requirePrompt = it }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Daily limit",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Once reached, opening requires a 15-second pause.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = days == 0 && minutes == 0,
                    onClick = { days = 0; minutes = 0 },
                    label = { Text("Off") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                PRESET_MINUTES.forEach { preset ->
                    FilterChip(
                        selected = days == 0 && minutes == preset,
                        onClick = { days = 0; minutes = preset },
                        label = { Text("$preset min") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = formatLimit(days, minutes),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))

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

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) { Text("Cancel") }
                TextButton(
                    onClick = {
                        val totalMinutes = days * 1440 + minutes
                        onSave(requirePrompt, totalMinutes.takeIf { it > 0 })
                    }
                ) { Text("Save") }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private fun formatLimit(days: Int, minutes: Int): String {
    if (days == 0 && minutes == 0) return "No limit"
    val parts = mutableListOf<String>()
    if (days > 0) parts += if (days == 1) "1 day" else "$days days"
    if (minutes > 0) {
        parts += if (minutes == 60) "1h" else "$minutes min"
    }
    return parts.joinToString(" ")
}
