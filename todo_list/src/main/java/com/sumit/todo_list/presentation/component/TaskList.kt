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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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

    val shape = RoundedCornerShape(22.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth(0.94f)
            .shadow(
                elevation = 14.dp,
                shape = shape,
                ambientColor = Color.Black,
                spotColor = Color.Black,
                clip = false
            )
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), shape)
            .padding(horizontal = 18.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Today's Tasks",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
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
                Text(text = "+", color = Color.White, fontSize = 20.sp)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        if (tasks.isEmpty()) {
            Text(
                text = "No tasks yet — tap + to add one",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        } else {
            tasks.forEach { task ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = task.isChecked,
                        onClick = { viewModel.toggleTask(task) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AccentPurple,
                            unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        modifier = Modifier.size(24.dp)
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
                            .padding(start = 12.dp)
                            .clickable { viewModel.toggleTask(task) }
                    )
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable { viewModel.deleteTask(task.id) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("×", color = DeleteRed, fontSize = 20.sp)
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
        shape = RoundedCornerShape(22.dp),
        title = { Text("New Task") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Task name") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (text.isNotBlank()) onConfirm(text) else onDismiss() }
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
