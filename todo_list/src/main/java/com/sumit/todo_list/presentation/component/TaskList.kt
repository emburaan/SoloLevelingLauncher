package com.sumit.todo_list.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.todo_list.R
import com.sumit.todo_list.presentation.TaskListViewModel

private val AccentPurple = Color(0xFF6C63FF)
private val AccentBlue = Color(0xFF42A5F5)
private val DeleteRed = Color(0xFFFF6B6B)

@Composable
fun TaskListSection(
    viewModel: TaskListViewModel = hiltViewModel(),
    onMaxReached: () -> Unit = {}
) {
    val tasks by viewModel.tasks.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        AddTaskDialog(
            onConfirm = { text ->
                val added = viewModel.addTask(text)
                if (!added) onMaxReached()
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }

    val shape = RoundedCornerShape(dimensionResource(R.dimen.task_card_corner))
    Column(
        modifier = Modifier
            .fillMaxWidth(0.94f)
            .shadow(
                elevation = dimensionResource(R.dimen.task_card_elevation),
                shape = shape,
                ambientColor = Color.Black,
                spotColor = Color.Black,
                clip = false
            )
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(
                dimensionResource(R.dimen.task_card_border),
                MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                shape
            )
            .padding(
                horizontal = dimensionResource(R.dimen.task_card_padding_h),
                vertical = dimensionResource(R.dimen.task_card_padding_v)
            ),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.task_list_title),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
            Box(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.task_add_button_size))
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(AccentPurple, AccentBlue)))
                    .clickable {
                        if (tasks.size >= TaskListViewModel.MAX_TASKS) {
                            onMaxReached()
                        } else {
                            showAddDialog = true
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.task_add_symbol),
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.task_header_spacing)))

        if (tasks.isEmpty()) {
            Text(
                text = stringResource(R.string.task_list_empty),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    vertical = dimensionResource(R.dimen.task_empty_padding_v)
                )
            )
        } else {
            tasks.forEach { task ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.task_row_padding_v))
                ) {
                    RadioButton(
                        selected = task.isChecked,
                        onClick = { viewModel.toggleTask(task) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AccentPurple,
                            unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        modifier = Modifier.size(dimensionResource(R.dimen.task_radio_size))
                    )
                    Text(
                        text = task.text,
                        color = if (task.isChecked)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = if (task.isChecked)
                                TextDecoration.LineThrough
                            else
                                TextDecoration.None
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = dimensionResource(R.dimen.task_text_start_padding))
                            .clickable { viewModel.toggleTask(task) }
                    )
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.task_delete_size))
                            .clip(CircleShape)
                            .clickable { viewModel.deleteTask(task.id) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.task_delete_symbol),
                            color = DeleteRed,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddTaskDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(dimensionResource(R.dimen.task_card_corner)),
        title = { Text(stringResource(R.string.task_add_dialog_title)) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(R.string.task_add_dialog_field_label)) },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (text.isNotBlank()) onConfirm(text) else onDismiss() }
            ) { Text(stringResource(R.string.task_action_add)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.task_action_cancel)) }
        }
    )
}
