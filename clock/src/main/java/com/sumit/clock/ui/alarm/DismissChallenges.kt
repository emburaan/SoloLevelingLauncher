package com.sumit.clock.ui.alarm

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sumit.clock.alarm.MathDifficulty
import com.sumit.clock.alarm.ShakeDetector
import com.sumit.clock.alarm.generateMathProblem

private val Light = Color(0xFFE0E0E8)
private val Muted = Color(0xFFB0B0B8)

@Composable
fun MathChallenge(
    problemCount: Int,
    difficulty: MathDifficulty,
    onComplete: () -> Unit
) {
    var solved by remember { mutableIntStateOf(0) }
    var problem by remember { mutableStateOf(generateMathProblem(difficulty)) }
    var input by remember { mutableStateOf("") }
    var wrong by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Problem ${solved + 1} of $problemCount",
            style = MaterialTheme.typography.labelMedium,
            color = Muted
        )
        Text(
            text = "${problem.text} = ?",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Light,
            color = Light
        )
        OutlinedTextField(
            value = input,
            onValueChange = {
                input = it.filter { ch -> ch.isDigit() || ch == '-' }
                wrong = false
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = wrong,
            modifier = Modifier.fillMaxWidth()
        )
        if (wrong) {
            Text(
                text = "Try again",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        Button(
            onClick = {
                val parsed = input.toIntOrNull()
                if (parsed == problem.answer) {
                    val next = solved + 1
                    if (next >= problemCount) {
                        onComplete()
                    } else {
                        solved = next
                        problem = generateMathProblem(difficulty)
                        input = ""
                    }
                } else {
                    wrong = true
                }
            },
            enabled = input.isNotBlank()
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun ShakeChallenge(
    targetCount: Int,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    var count by remember { mutableIntStateOf(0) }

    DisposableEffect(Unit) {
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val accelerometer = sm?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val detector = ShakeDetector {
            val next = count + 1
            count = next
            if (next >= targetCount) onComplete()
        }
        if (sm != null && accelerometer != null) {
            sm.registerListener(detector, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        }
        onDispose {
            sm?.unregisterListener(detector)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Shake the phone vigorously",
            style = MaterialTheme.typography.titleMedium,
            color = Light,
            textAlign = TextAlign.Center
        )
        Text(
            text = "$count / $targetCount",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Light,
            color = Light
        )
        LinearProgressIndicator(
            progress = { (count.toFloat() / targetCount.toFloat()).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun TypingChallenge(
    phrase: String,
    onComplete: () -> Unit
) {
    var input by remember { mutableStateOf("") }
    val target = phrase.trim()
    val matches = input.trim().equals(target, ignoreCase = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Type to dismiss",
            style = MaterialTheme.typography.labelMedium,
            color = Muted
        )
        Text(
            text = target,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Light,
            color = Light,
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            singleLine = false,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onComplete,
            enabled = matches
        ) {
            Text("Submit")
        }
    }
}
