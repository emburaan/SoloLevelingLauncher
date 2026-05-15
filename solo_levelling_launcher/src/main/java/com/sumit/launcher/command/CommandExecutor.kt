package com.sumit.launcher.command

import android.content.Context
import android.content.Intent
import com.sumit.launcher.data.focus.AppFocusRepository
import com.sumit.todo_list.data.repository.TaskRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommandExecutor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val taskRepository: TaskRepository,
    private val focusRepository: AppFocusRepository
) {
    sealed interface Result {
        data class Ok(val message: String) : Result
        data class Failed(val message: String) : Result
    }

    suspend fun execute(command: Command): Result = when (command) {
        is Command.LaunchApp -> runLaunch(command)
        is Command.AddTask -> runAddTask(command)
        is Command.SetLimit -> runSetLimit(command)
        is Command.Unknown -> Result.Failed(
            "Didn't understand. Try \"open spotify\", \"add quest run 5km\", or \"limit instagram 20\"."
        )
    }

    private fun runLaunch(cmd: Command.LaunchApp): Result {
        val match = resolveApp(cmd.query)
            ?: return Result.Failed("No app matching \"${cmd.query}\"")
        val intent = context.packageManager.getLaunchIntentForPackage(match.packageName)
            ?: return Result.Failed("Can't launch \"${match.label}\"")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        return Result.Ok("Opening ${match.label}")
    }

    private suspend fun runAddTask(cmd: Command.AddTask): Result {
        taskRepository.addTask(cmd.text)
        return Result.Ok("Added quest: \"${cmd.text}\"")
    }

    private fun runSetLimit(cmd: Command.SetLimit): Result {
        val match = resolveApp(cmd.query)
            ?: return Result.Failed("No app matching \"${cmd.query}\"")
        focusRepository.setDailyLimit(match.packageName, cmd.minutes)
        return Result.Ok("Limit on ${match.label}: ${cmd.minutes} min/day")
    }

    private fun resolveApp(query: String): AppMatch? {
        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val resolved = pm.queryIntentActivities(mainIntent, 0)
        val q = query.lowercase().trim()
        if (q.isEmpty()) return null
        return resolved
            .mapNotNull { ri ->
                val label = ri.loadLabel(pm).toString()
                val pkg = ri.activityInfo.packageName
                val l = label.lowercase()
                val score = when {
                    l == q -> 100
                    l.startsWith(q) -> 80
                    l.contains(q) -> 60
                    pkg.contains(q) -> 40
                    else -> 0
                }
                if (score > 0) AppMatch(label, pkg) to score else null
            }
            .maxByOrNull { it.second }
            ?.first
    }

    private data class AppMatch(val label: String, val packageName: String)
}
