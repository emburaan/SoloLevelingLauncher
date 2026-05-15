package com.sumit.sololevelinglauncher.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.sumit.todo_list.data.local.TaskDatabase

class DeleteTaskAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val taskId = parameters[TaskIdParam] ?: return
        TaskDatabase.getInstance(context).taskDao().deleteById(taskId)
        TodoGlanceWidget().update(context, glanceId)
    }
}
