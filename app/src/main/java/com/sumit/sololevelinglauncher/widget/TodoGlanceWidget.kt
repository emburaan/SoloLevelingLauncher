package com.sumit.sololevelinglauncher.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.sumit.sololevelinglauncher.R
import com.sumit.todo_list.data.local.TaskDatabase
import com.sumit.todo_list.data.local.TaskEntity

internal val TaskIdParam = ActionParameters.Key<Int>("taskId")

class TodoGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val dao = TaskDatabase.getInstance(context).taskDao()
        provideContent {
            val tasks by dao.observeAll().collectAsState(initial = emptyList())
            WidgetContent(tasks)
        }
    }

    @Composable
    private fun WidgetContent(tasks: List<TaskEntity>) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ImageProvider(R.drawable.widget_bg))
                .padding(14.dp)
        ) {
            Header()
            Spacer(modifier = GlanceModifier.height(8.dp))
            if (tasks.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(modifier = GlanceModifier.fillMaxSize()) {
                    items(tasks, itemId = { it.id.toLong() }) { task ->
                        TaskRow(task)
                    }
                }
            }
        }
    }

    @Composable
    private fun Header() {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today's Tasks",
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = GlanceModifier.defaultWeight()
            )
            Box(
                modifier = GlanceModifier
                    .size(32.dp)
                    .background(ImageProvider(R.drawable.widget_button_bg))
                    .clickable(actionStartActivity<AddTaskActivity>()),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    provider = ImageProvider(R.drawable.widget_ic_add),
                    contentDescription = "Add task",
                    modifier = GlanceModifier.size(18.dp)
                )
            }
        }
    }

    @Composable
    private fun EmptyState() {
        Box(
            modifier = GlanceModifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No tasks yet — tap + to add one",
                style = TextStyle(
                    color = ColorProvider(Color(0x99FFFFFF.toInt())),
                    fontSize = 13.sp
                )
            )
        }
    }

    @Composable
    private fun TaskRow(task: TaskEntity) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable(
                    actionRunCallback<ToggleTaskAction>(
                        actionParametersOf(TaskIdParam to task.id)
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                provider = ImageProvider(
                    if (task.isChecked) R.drawable.widget_ic_radio_on
                    else R.drawable.widget_ic_radio_off
                ),
                contentDescription = null,
                modifier = GlanceModifier.size(22.dp)
            )
            Spacer(modifier = GlanceModifier.width(10.dp))
            Text(
                text = task.text,
                style = TextStyle(
                    color = ColorProvider(
                        if (task.isChecked) Color(0x80FFFFFF.toInt()) else Color.White
                    ),
                    fontSize = 14.sp,
                    textDecoration = if (task.isChecked) TextDecoration.LineThrough else TextDecoration.None
                ),
                maxLines = 2,
                modifier = GlanceModifier.defaultWeight()
            )
            Box(
                modifier = GlanceModifier
                    .size(28.dp)
                    .clickable(
                        actionRunCallback<DeleteTaskAction>(
                            actionParametersOf(TaskIdParam to task.id)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    provider = ImageProvider(R.drawable.widget_ic_delete),
                    contentDescription = "Delete task",
                    modifier = GlanceModifier.size(18.dp)
                )
            }
        }
    }
}
