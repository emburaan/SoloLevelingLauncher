package com.sumit.launcher.command

sealed interface Command {
    data class LaunchApp(val query: String) : Command
    data class AddTask(val text: String) : Command
    data class SetLimit(val query: String, val minutes: Int) : Command
    data class Unknown(val raw: String) : Command
}
