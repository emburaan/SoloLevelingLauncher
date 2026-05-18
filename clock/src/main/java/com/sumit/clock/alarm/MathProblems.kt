package com.sumit.clock.alarm

import kotlin.random.Random

fun generateMathProblem(difficulty: MathDifficulty): MathProblem = when (difficulty) {
    MathDifficulty.Easy -> {
        val a = Random.nextInt(2, 20)
        val b = Random.nextInt(2, 20)
        if (Random.nextBoolean()) MathProblem("$a + $b", a + b)
        else MathProblem("${a + b} - $b", a)
    }
    MathDifficulty.Medium -> when (Random.nextInt(3)) {
        0 -> {
            val a = Random.nextInt(11, 80)
            val b = Random.nextInt(11, 40)
            MathProblem("$a + $b", a + b)
        }
        1 -> {
            val a = Random.nextInt(40, 99)
            val b = Random.nextInt(11, 39)
            MathProblem("$a - $b", a - b)
        }
        else -> {
            val x = Random.nextInt(3, 13)
            val y = Random.nextInt(3, 13)
            MathProblem("$x × $y", x * y)
        }
    }
    MathDifficulty.Hard -> when (Random.nextInt(4)) {
        0 -> {
            val a = Random.nextInt(100, 999)
            val b = Random.nextInt(100, 999)
            MathProblem("$a + $b", a + b)
        }
        1 -> {
            val a = Random.nextInt(500, 999)
            val b = Random.nextInt(100, 499)
            MathProblem("$a - $b", a - b)
        }
        2 -> {
            val x = Random.nextInt(11, 30)
            val y = Random.nextInt(11, 30)
            MathProblem("$x × $y", x * y)
        }
        else -> {
            val q = Random.nextInt(2, 15)
            val d = Random.nextInt(2, 15)
            MathProblem("${q * d} ÷ $d", q)
        }
    }
}
