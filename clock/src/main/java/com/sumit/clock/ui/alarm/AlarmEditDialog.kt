package com.sumit.clock.ui.alarm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sumit.clock.R
import com.sumit.clock.alarm.Alarm
import com.sumit.clock.alarm.DismissDefaults
import com.sumit.clock.alarm.MathDifficulty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmEditDialog(
    initial: Alarm,
    onDismiss: () -> Unit,
    onSave: (Alarm) -> Unit,
    onDelete: (() -> Unit)?
) {
    val timeState = rememberTimePickerState(
        initialHour = initial.hour,
        initialMinute = initial.minute,
        is24Hour = true
    )
    var label by remember { mutableStateOf(initial.label) }
    var repeatDaily by remember { mutableStateOf(initial.repeatDaily) }
    var mathProblems by remember { mutableIntStateOf(initial.mathProblems) }
    var mathDifficulty by remember { mutableStateOf(initial.mathDifficulty) }
    var shakeCount by remember { mutableIntStateOf(initial.shakeCount) }
    var typingPhrase by remember { mutableStateOf(initial.typingPhrase) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .heightIn(max = 640.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(
                        if (initial.id == Alarm.NEW_ID) R.string.alarm_new_title
                        else R.string.alarm_edit_title
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TimePicker(state = timeState)
                }

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text(stringResource(R.string.alarm_label_hint)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.alarm_repeat_daily),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Switch(checked = repeatDaily, onCheckedChange = { repeatDaily = it })
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.alarm_dismiss_mode_title),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.alarm_dismiss_random_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.alarm_dismiss_section_math),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                MathConfig(
                    problems = mathProblems,
                    difficulty = mathDifficulty,
                    onProblemsChange = { mathProblems = it.coerceIn(1, 10) },
                    onDifficultyChange = { mathDifficulty = it }
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.alarm_dismiss_section_shake),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ShakeConfig(
                    count = shakeCount,
                    onCountChange = { shakeCount = it.coerceIn(5, 100) }
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.alarm_dismiss_section_typing),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TypingConfig(
                    phrase = typingPhrase,
                    onPhraseChange = { typingPhrase = it }
                )

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (onDelete != null) {
                        TextButton(
                            onClick = onDelete,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(stringResource(R.string.alarm_delete))
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.alarm_cancel))
                    }
                    TextButton(
                        onClick = {
                            val isNew = initial.id == Alarm.NEW_ID
                            onSave(
                                initial.copy(
                                    hour = timeState.hour,
                                    minute = timeState.minute,
                                    label = label.trim(),
                                    repeatDaily = repeatDaily,
                                    enabled = if (isNew) true else initial.enabled,
                                    mathProblems = mathProblems,
                                    mathDifficulty = mathDifficulty,
                                    shakeCount = shakeCount,
                                    typingPhrase = typingPhrase.trim()
                                        .ifEmpty { DismissDefaults.TYPING_PHRASE }
                                )
                            )
                        }
                    ) {
                        Text(stringResource(R.string.alarm_save))
                    }
                }
            }
        }
    }
}

@Composable
private fun MathConfig(
    problems: Int,
    difficulty: MathDifficulty,
    onProblemsChange: (Int) -> Unit,
    onDifficultyChange: (MathDifficulty) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.alarm_math_problems_label),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            StepperControl(
                value = problems,
                min = 1,
                max = 10,
                onChange = onProblemsChange
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DifficultyChip(MathDifficulty.Easy, difficulty, onDifficultyChange)
            DifficultyChip(MathDifficulty.Medium, difficulty, onDifficultyChange)
            DifficultyChip(MathDifficulty.Hard, difficulty, onDifficultyChange)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DifficultyChip(
    value: MathDifficulty,
    selected: MathDifficulty,
    onChange: (MathDifficulty) -> Unit
) {
    val labelRes = when (value) {
        MathDifficulty.Easy -> R.string.alarm_math_easy
        MathDifficulty.Medium -> R.string.alarm_math_medium
        MathDifficulty.Hard -> R.string.alarm_math_hard
    }
    FilterChip(
        selected = value == selected,
        onClick = { onChange(value) },
        label = { Text(stringResource(labelRes)) }
    )
}

@Composable
private fun ShakeConfig(count: Int, onCountChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.alarm_shake_count_label),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        StepperControl(
            value = count,
            min = 5,
            max = 100,
            step = 5,
            onChange = onCountChange
        )
    }
}

@Composable
private fun TypingConfig(phrase: String, onPhraseChange: (String) -> Unit) {
    OutlinedTextField(
        value = phrase,
        onValueChange = onPhraseChange,
        label = { Text(stringResource(R.string.alarm_typing_phrase_label)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun StepperControl(
    value: Int,
    min: Int,
    max: Int,
    step: Int = 1,
    onChange: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextButton(
            onClick = { onChange((value - step).coerceAtLeast(min)) },
            enabled = value > min
        ) { Text("−") }
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        TextButton(
            onClick = { onChange((value + step).coerceAtMost(max)) },
            enabled = value < max
        ) { Text("+") }
    }
}
