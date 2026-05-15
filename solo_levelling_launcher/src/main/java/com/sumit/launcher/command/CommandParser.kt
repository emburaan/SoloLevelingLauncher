package com.sumit.launcher.command

object CommandParser {

    private const val SYSTEM_PROMPT =
        "You are a launcher command parser. " +
            "Given the user's input, output EXACTLY ONE line in this format and nothing else:\n" +
            "ACTION|ARG1|ARG2\n\n" +
            "Available actions:\n" +
            "- launch|<app_name>          (open an app)\n" +
            "- task|<task_text>           (add a quest/todo)\n" +
            "- limit|<app_name>|<minutes> (set a daily app limit)\n\n" +
            "Examples:\n" +
            "Input: open spotify\nOutput: launch|spotify\n" +
            "Input: launch instagram\nOutput: launch|instagram\n" +
            "Input: add quest go for a run\nOutput: task|go for a run\n" +
            "Input: remind me to call mom\nOutput: task|call mom\n" +
            "Input: limit instagram to 20 minutes\nOutput: limit|instagram|20\n" +
            "Input: set youtube limit 45 min\nOutput: limit|youtube|45\n\n" +
            "Output ONLY the line, no explanation."

    /** Builds a Qwen2.5 chat-template prompt that asks the model to emit one command line. */
    fun buildPrompt(userInput: String): String = buildString {
        append("<|im_start|>system\n")
        append(SYSTEM_PROMPT)
        append("<|im_end|>\n")
        append("<|im_start|>user\n")
        append(userInput.trim())
        append("<|im_end|>\n")
        append("<|im_start|>assistant\n")
    }

    /** Parses the model's raw output (may include trailing tokens, whitespace, fences). */
    fun parse(raw: String): Command {
        val firstLine = raw
            .lineSequence()
            .map { it.trim().trim('`') }
            .firstOrNull { it.isNotEmpty() }
            ?: return Command.Unknown(raw)

        val parts = firstLine.split('|').map { it.trim() }
        val action = parts.firstOrNull()?.lowercase().orEmpty()

        return when (action) {
            "launch" -> parts.getOrNull(1)
                ?.takeIf { it.isNotBlank() }
                ?.let { Command.LaunchApp(it) }
                ?: Command.Unknown(raw)
            "task" -> parts.drop(1)
                .joinToString(" ")
                .trim()
                .takeIf { it.isNotBlank() }
                ?.let { Command.AddTask(it) }
                ?: Command.Unknown(raw)
            "limit" -> {
                val name = parts.getOrNull(1)?.takeIf { it.isNotBlank() }
                val mins = parts.getOrNull(2)?.filter { it.isDigit() }?.toIntOrNull()
                if (name != null && mins != null && mins > 0) {
                    Command.SetLimit(name, mins)
                } else {
                    Command.Unknown(raw)
                }
            }
            else -> Command.Unknown(raw)
        }
    }
}
