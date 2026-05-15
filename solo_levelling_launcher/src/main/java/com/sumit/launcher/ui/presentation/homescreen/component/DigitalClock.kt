package com.sumit.launcher.ui.presentation.homescreen.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.sumit.sololevelinglauncher.ui.theme.SLAccentPurple
import com.sumit.sololevelinglauncher.ui.theme.SLTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NeumorphicAnalogClock(modifier: Modifier) {
    var calendar by remember { mutableStateOf(Calendar.getInstance()) }
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (isActive) {
                calendar = Calendar.getInstance()
                delay(1000)
            }
        }
    }

    val face = MaterialTheme.colorScheme.surfaceContainerHigh
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val tones = SLTheme.tones

    Box(
        modifier = modifier
            .size(180.dp)
            .shadow(
                elevation = 18.dp,
                shape = CircleShape,
                ambientColor = tones.shadow,
                spotColor = tones.shadow,
                clip = false
            )
            .background(face, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(160.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f

            // Subtle inner highlight / depth (bevel)
            drawCircle(
                color = tones.shadow,
                radius = radius,
                center = Offset(center.x + 6, center.y + 6),
                alpha = 0.20f
            )
            drawCircle(
                color = tones.highlight,
                radius = radius,
                center = Offset(center.x - 6, center.y - 6),
                alpha = 0.10f
            )

            // Clock rim
            drawCircle(
                color = face,
                radius = radius * 0.98f,
                center = center,
                style = Stroke(width = 6f)
            )

            // Hour marks
            for (i in 0 until 12) {
                val angle = Math.toRadians((i * 30 - 90).toDouble())
                val start = Offset(
                    x = (center.x + cos(angle) * (radius - 24.dp.toPx())).toFloat(),
                    y = (center.y + sin(angle) * (radius - 24.dp.toPx())).toFloat()
                )
                val end = Offset(
                    x = (center.x + cos(angle) * (radius - 8.dp.toPx())).toFloat(),
                    y = (center.y + sin(angle) * (radius - 8.dp.toPx())).toFloat()
                )
                drawLine(
                    color = if (i % 3 == 0) onSurface else onSurfaceVariant,
                    start = start,
                    end = end,
                    strokeWidth = if (i % 3 == 0) 4f else 2f,
                    cap = StrokeCap.Round
                )
            }

            val hour = calendar.get(Calendar.HOUR)
            val minute = calendar.get(Calendar.MINUTE)
            val second = calendar.get(Calendar.SECOND)

            val hourAngle = ((hour + minute / 60f) * 30 - 90) * (PI / 180)
            val minuteAngle = ((minute + second / 60f) * 6 - 90) * (PI / 180)
            val secondAngle = (second * 6 - 90) * (PI / 180)

            drawLine(
                color = SLAccentPurple,
                start = center,
                end = Offset(
                    x = (center.x + cos(hourAngle) * (radius * 0.45f)).toFloat(),
                    y = (center.y + sin(hourAngle) * (radius * 0.45f)).toFloat()
                ),
                strokeWidth = 8f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = onSurface,
                start = center,
                end = Offset(
                    x = (center.x + cos(minuteAngle) * (radius * 0.65f)).toFloat(),
                    y = (center.y + sin(minuteAngle) * (radius * 0.65f)).toFloat()
                ),
                strokeWidth = 5f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = SLAccentPurple.copy(alpha = 0.7f),
                start = center,
                end = Offset(
                    x = (center.x + cos(secondAngle) * (radius * 0.75f)).toFloat(),
                    y = (center.y + sin(secondAngle) * (radius * 0.75f)).toFloat()
                ),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )
            drawCircle(
                color = SLAccentPurple,
                radius = 8.dp.toPx(),
                center = center
            )
        }
    }
}
