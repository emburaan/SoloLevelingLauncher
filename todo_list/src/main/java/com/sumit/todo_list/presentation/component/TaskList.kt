package com.sumit.todo_list.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TaskListSection() {
    val tasks = remember {
        mutableStateListOf(
            TaskItem("Read Solo Leveling", false),
            TaskItem("Workout", false),
            TaskItem("Finish project", false),
            TaskItem("Call Sung Jinwoo", false)
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .shadow(8.dp, shape = MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.8f),
                        Color.Black.copy(alpha = 0.6f)
                    )
                )
            )
            .padding(18.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Today's Tasks",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        tasks.forEachIndexed { id, task ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .clickable {
                        tasks[id] = task.copy(isChecked = !task.isChecked)
                    }
            ) {
                RadioButton(
                    selected = task.isChecked,
                    onClick = {
                        tasks[id] = task.copy(isChecked = !task.isChecked)
                    },
                    colors = androidx.compose.material3.RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF6C63FF),
                        unselectedColor = Color.White,
                        disabledSelectedColor = Color.Gray,
                        disabledUnselectedColor = Color.LightGray
                    ),
                    modifier = Modifier
                        .size(24.dp)
                )
                Text(
                    text = task.text,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}