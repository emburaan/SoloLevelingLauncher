package com.sumit.clock.alarm

data class Alarm(
    val id: Long,
    val hour: Int,
    val minute: Int,
    val label: String,
    val enabled: Boolean,
    val repeatDaily: Boolean,
    val mathProblems: Int = DismissDefaults.MATH_PROBLEMS,
    val mathDifficulty: MathDifficulty = DismissDefaults.MATH_DIFFICULTY,
    val shakeCount: Int = DismissDefaults.SHAKE_COUNT,
    val typingPhrase: String = DismissDefaults.TYPING_PHRASE
) {
    companion object {
        const val NEW_ID = 0L
        fun new(hour: Int = 7, minute: Int = 0): Alarm =
            Alarm(NEW_ID, hour, minute, "", enabled = true, repeatDaily = false)
    }
}
