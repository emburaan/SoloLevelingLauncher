package com.sumit.clock.alarm

import android.content.Context
import android.content.Intent
import android.graphics.Color as AndroidColor
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sumit.clock.R
import com.sumit.clock.ui.alarm.AlarmTheme
import com.sumit.clock.ui.alarm.MathChallenge
import com.sumit.clock.ui.alarm.ShakeChallenge
import com.sumit.clock.ui.alarm.TypingChallenge

class AlarmRingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
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
        volumeControlStream = AudioManager.STREAM_ALARM
        pinAlarmVolumeToMax()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = Unit
        })

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
            AlarmTheme {
                AlarmRingContent(
                    hour = hour,
                    minute = minute,
                    label = label,
                    mathProblems = mathProblems,
                    mathDifficulty = mathDifficulty,
                    shakeCount = shakeCount,
                    typingPhrase = typingPhrase,
                    onDismiss = {
                        AlarmRingService.stop(this)
                        finishAndRemoveTask()
                    }
                )
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (isVolumeKey(keyCode)) {
            pinAlarmVolumeToMax()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (isVolumeKey(keyCode)) {
            pinAlarmVolumeToMax()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) pinAlarmVolumeToMax()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        val relaunch = Intent(this, AlarmRingActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
        }
        runCatching { startActivity(relaunch) }
    }

    private fun isVolumeKey(keyCode: Int): Boolean =
        keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
            keyCode == KeyEvent.KEYCODE_VOLUME_UP ||
            keyCode == KeyEvent.KEYCODE_VOLUME_MUTE

    private fun pinAlarmVolumeToMax() {
        val am = getSystemService(Context.AUDIO_SERVICE) as? AudioManager ?: return
        runCatching {
            val max = am.getStreamMaxVolume(AudioManager.STREAM_ALARM)
            am.setStreamVolume(AudioManager.STREAM_ALARM, max, 0)
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
    onDismiss: () -> Unit
) {
    val dismissMode = remember { DismissMode.entries.random() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 48.dp, bottom = 48.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.alarm_ring_title),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "%02d:%02d".format(hour, minute),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (label.isNotBlank()) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
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
    }
}
