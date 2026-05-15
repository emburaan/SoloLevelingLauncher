package com.sumit.sololevelinglauncher.widget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.sumit.todo_list.data.local.TaskDatabase
import com.sumit.todo_list.data.local.TaskEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFinishOnTouchOutside(true)
        setContent {
            MaterialTheme {
                AddTaskDialogContent(
                    onConfirm = { text ->
                        saveTask(text)
                        finish()
                    },
                    onCancel = { finish() }
                )
            }
        }
    }

    private fun saveTask(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return
        val appContext = applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            TaskDatabase.getInstance(appContext).taskDao()
                .insert(TaskEntity(text = trimmed))
            val widget = TodoGlanceWidget()
            GlanceAppWidgetManager(appContext)
                .getGlanceIds(TodoGlanceWidget::class.java)
                .forEach { widget.update(appContext, it) }
        }
    }
}

@Composable
private fun AddTaskDialogContent(
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "New Task",
            style = MaterialTheme.typography.titleMedium
        )
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Task name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onConfirm(text) }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) { Text("Cancel") }
            TextButton(
                onClick = { if (text.isNotBlank()) onConfirm(text) else onCancel() }
            ) { Text("Add") }
        }
    }
}
