package com.sumit.sololevelinglauncher.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.sumit.todo_list.data.local.TaskDatabase

class ToggleTaskAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val taskId = parameters[TaskIdParam] ?: return
        val dao = TaskDatabase.getInstance(context).taskDao()
        val task = dao.getAll().firstOrNull { it.id == taskId } ?: return
        dao.update(task.copy(isChecked = !task.isChecked))
        TodoGlanceWidget().update(context, glanceId)
    }
}
