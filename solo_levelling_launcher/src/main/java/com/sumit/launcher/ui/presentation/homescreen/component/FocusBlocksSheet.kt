package com.sumit.launcher.ui.presentation.homescreen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.launcher.R
import com.sumit.launcher.data.focus.FocusBlock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusBlocksSheet(
    onDismiss: () -> Unit,
    viewModel: FocusBlocksViewModel = hiltViewModel()
) {
    val blocks by viewModel.blocks.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var editingBlock by remember { mutableStateOf<FocusBlock?>(null) }
    var showEditor by remember { mutableStateOf(false) }

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
                text = stringResource(R.string.focus_blocks_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.focus_blocks_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_3xl)))

            if (blocks.isEmpty()) {
                Text(
                    text = stringResource(R.string.focus_blocks_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                blocks.forEach { block ->
                    BlockRow(
                        block = block,
                        onClick = {
                            editingBlock = block
                            showEditor = true
                        },
                        onToggle = { viewModel.upsert(block.copy(enabled = it)) },
                        onDelete = { viewModel.delete(block.id) }
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_sm)))
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
                TextButton(onClick = {
                    editingBlock = null
                    showEditor = true
                }) {
                    Text(stringResource(R.string.action_add))
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_md)))
        }
    }

    if (showEditor) {
        FocusBlockEditor(
            initial = editingBlock,
            onSave = { viewModel.upsert(it); showEditor = false },
            onDismiss = { showEditor = false }
        )
    }
}

@Composable
private fun BlockRow(
    block: FocusBlock,
    onClick: () -> Unit,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val isActiveNow = block.isActive()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = dimensionResource(R.dimen.spacing_sm)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xl))
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(
                        R.string.focus_block_time_range,
                        formatTime(block.startMinute),
                        formatTime(block.endMinute)
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (isActiveNow) {
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_md)))
                    Text(
                        text = stringResource(R.string.focus_block_active_now),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(
                text = formatDays(block.daysMask),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = block.enabled, onCheckedChange = onToggle)
        Text(
            text = "✕",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .clickable { onDelete() }
                .padding(dimensionResource(R.dimen.spacing_md))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FocusBlockEditor(
    initial: FocusBlock?,
    onSave: (FocusBlock) -> Unit,
    onDismiss: () -> Unit
) {
    val startState = rememberTimePickerState(
        initialHour = (initial?.startMinute ?: 9 * 60) / 60,
        initialMinute = (initial?.startMinute ?: 9 * 60) % 60,
        is24Hour = true
    )
    val endState = rememberTimePickerState(
        initialHour = (initial?.endMinute ?: 12 * 60) / 60,
        initialMinute = (initial?.endMinute ?: 12 * 60) % 60,
        is24Hour = true
    )
    var daysMask by remember {
        mutableIntStateOf(initial?.daysMask ?: FocusBlock.WEEKDAYS_MASK)
    }
    var editingEnd by remember { mutableStateOf(false) }
    val dayLabels = dayLabelInitials()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_dialog)),
        title = {
            Text(
                stringResource(
                    if (initial == null) R.string.focus_block_add_title
                    else R.string.focus_block_edit_title
                )
            )
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.spacing_md)
                    )
                ) {
                    FilterChip(
                        selected = !editingEnd,
                        onClick = { editingEnd = false },
                        label = {
                            Text(
                                stringResource(
                                    R.string.focus_block_start_chip,
                                    formatTime(startState.hour * 60 + startState.minute)
                                )
                            )
                        }
                    )
                    FilterChip(
                        selected = editingEnd,
                        onClick = { editingEnd = true },
                        label = {
                            Text(
                                stringResource(
                                    R.string.focus_block_end_chip,
                                    formatTime(endState.hour * 60 + endState.minute)
                                )
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))
                TimeInput(state = if (editingEnd) endState else startState)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_md)))
                Text(
                    stringResource(R.string.focus_block_days_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_sm)))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.spacing_xs)
                    )
                ) {
                    dayLabels.forEachIndexed { idx, label ->
                        val bit = 1 shl idx
                        FilterChip(
                            selected = (daysMask and bit) != 0,
                            onClick = { daysMask = daysMask xor bit },
                            label = { Text(label) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val start = startState.hour * 60 + startState.minute
                    val end = endState.hour * 60 + endState.minute
                    if (daysMask == 0) return@TextButton
                    val block = (initial ?: FocusBlock(
                        startMinute = start,
                        endMinute = end,
                        daysMask = daysMask
                    )).copy(
                        startMinute = start,
                        endMinute = end,
                        daysMask = daysMask
                    )
                    onSave(block)
                }
            ) { Text(stringResource(R.string.action_save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}

@Composable
private fun dayLabelInitials(): List<String> = listOf(
    stringResource(R.string.day_initial_mon),
    stringResource(R.string.day_initial_tue),
    stringResource(R.string.day_initial_wed),
    stringResource(R.string.day_initial_thu),
    stringResource(R.string.day_initial_fri),
    stringResource(R.string.day_initial_sat),
    stringResource(R.string.day_initial_sun)
)

private fun formatTime(minutesSinceMidnight: Int): String {
    val h = (minutesSinceMidnight / 60).coerceIn(0, 23)
    val m = (minutesSinceMidnight % 60).coerceIn(0, 59)
    return "%02d:%02d".format(h, m)
}

@Composable
private fun formatDays(daysMask: Int): String {
    if (daysMask == 0) return stringResource(R.string.focus_block_days_none)
    if (daysMask == FocusBlock.ALL_DAYS_MASK)
        return stringResource(R.string.focus_block_days_every)
    if (daysMask == FocusBlock.WEEKDAYS_MASK)
        return stringResource(R.string.focus_block_days_weekdays)
    if (daysMask == FocusBlock.WEEKEND_MASK)
        return stringResource(R.string.focus_block_days_weekends)
    val labels = dayLabelInitials()
    return labels.mapIndexedNotNull { idx, label ->
        if ((daysMask and (1 shl idx)) != 0) label else null
    }.joinToString(" ")
}
