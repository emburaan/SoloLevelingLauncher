package com.sumit.clock.alarm

enum class DismissMode { Math, Shake, Typing }

enum class MathDifficulty { Easy, Medium, Hard }

data class MathProblem(val text: String, val answer: Int)

object DismissDefaults {
    const val MATH_PROBLEMS = 3
    val MATH_DIFFICULTY = MathDifficulty.Easy
    const val SHAKE_COUNT = 20
    const val TYPING_PHRASE = "I am awake"
}
