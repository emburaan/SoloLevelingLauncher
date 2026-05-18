package com.sumit.clock.alarm

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sumit.clock.R
import com.sumit.clock.ui.alarm.MathChallenge
import com.sumit.clock.ui.alarm.ShakeChallenge
import com.sumit.clock.ui.alarm.TypingChallenge

class AlarmRingActivity : ComponentActivity() {

    private var ringer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        startRinger()

        val alarmId = intent.getLongExtra(AlarmReceiver.EXTRA_ALARM_ID, -1L)
        val label = intent.getStringExtra(AlarmReceiver.EXTRA_ALARM_LABEL).orEmpty()
        val hour = intent.getIntExtra(AlarmReceiver.EXTRA_ALARM_HOUR, 0)
        val minute = intent.getIntExtra(AlarmReceiver.EXTRA_ALARM_MINUTE, 0)
        val mathProblems = intent.getIntExtra(
            AlarmReceiver.EXTRA_MATH_PROBLEMS, DismissDefaults.MATH_PROBLEMS
        )
        val mathDifficulty = runCatching {
            MathDifficulty.valueOf(
                intent.getStringExtra(AlarmReceiver.EXTRA_MATH_DIFFICULTY)
                    ?: DismissDefaults.MATH_DIFFICULTY.name
            )
        }.getOrDefault(DismissDefaults.MATH_DIFFICULTY)
        val shakeCount = intent.getIntExtra(
            AlarmReceiver.EXTRA_SHAKE_COUNT, DismissDefaults.SHAKE_COUNT
        )
        val typingPhrase = intent.getStringExtra(AlarmReceiver.EXTRA_TYPING_PHRASE)
            ?: DismissDefaults.TYPING_PHRASE

        setContent {
            AlarmRingContent(
                hour = hour,
                minute = minute,
                label = label,
                mathProblems = mathProblems,
                mathDifficulty = mathDifficulty,
                shakeCount = shakeCount,
                typingPhrase = typingPhrase,
                onSnooze = {
                    if (alarmId >= 0) {
                        val snoozeIntent = Intent(this, AlarmReceiver::class.java).apply {
                            action = AlarmReceiver.ACTION_SNOOZE
                            putExtra(AlarmReceiver.EXTRA_ALARM_ID, alarmId)
                        }
                        sendBroadcast(snoozeIntent)
                    }
                    finishAndRemoveTask()
                },
                onDismiss = { finishAndRemoveTask() }
            )
        }
    }

    override fun onDestroy() {
        ringer?.let { mp ->
            runCatching { mp.stop() }
            runCatching { mp.release() }
        }
        ringer = null
        super.onDestroy()
    }

    private fun startRinger() {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            ?: return
        runCatching {
            ringer = MediaPlayer().apply {
                setDataSource(this@AlarmRingActivity, uri)
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                isLooping = true
                prepare()
                start()
            }
        }
    }
}

@Composable
private fun AlarmRingContent(
    hour: Int,
    minute: Int,
    label: String,
    mathProblems: Int,
    mathDifficulty: MathDifficulty,
    shakeCount: Int,
    typingPhrase: String,
    onSnooze: () -> Unit,
    onDismiss: () -> Unit
) {
    val dismissMode = remember { DismissMode.entries.random() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101015))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 48.dp, bottom = 120.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.alarm_ring_title),
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFB0B0B8)
            )
            Text(
                text = "%02d:%02d".format(hour, minute),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Light,
                color = Color.White
            )
            if (label.isNotBlank()) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFE0E0E8),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            when (dismissMode) {
                DismissMode.Math -> MathChallenge(
                    problemCount = mathProblems,
                    difficulty = mathDifficulty,
                    onComplete = onDismiss
                )
                DismissMode.Shake -> ShakeChallenge(
                    targetCount = shakeCount,
                    onComplete = onDismiss
                )
                DismissMode.Typing -> TypingChallenge(
                    phrase = typingPhrase,
                    onComplete = onDismiss
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(onClick = onSnooze) {
                Text(stringResource(R.string.alarm_snooze))
            }
        }
    }
}
