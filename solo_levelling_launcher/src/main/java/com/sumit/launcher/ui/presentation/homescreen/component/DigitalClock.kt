package com.sumit.launcher.ui.presentation.homescreen.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.sumit.sololevelinglauncher.ui.theme.SLBackground
import com.sumit.sololevelinglauncher.ui.theme.SLText
import kotlinx.coroutines.delay
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NeumorphicAnalogClock(modifier: Modifier) {
    var calendar by remember { mutableStateOf(Calendar.getInstance()) }
    LaunchedEffect(Unit) {
        while (true) {
            calendar = Calendar.getInstance()
            delay(1000)
        }
    }
    Box(
        modifier = modifier
            .size(180.dp)
            .shadow(
                16.dp,
                CircleShape,
                ambientColor = Color(0xFF23243A),
                spotColor = Color(0xFF23243A)
            )
            .background(SLBackground, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(160.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f

            // Neumorphic effect: draw two soft shadows
            drawCircle(
                color = Color(0xFF23243A),
                radius = radius,
                center = Offset(center.x + 8, center.y + 8),
                alpha = 0.18f
            )
            drawCircle(
                color = Color.White,
                radius = radius,
                center = Offset(center.x - 8, center.y - 8),
                alpha = 0.10f
            )

            // Clock face
            drawCircle(
                color = SLBackground,
                radius = radius * 0.98f,
                center = center,
                style = Stroke(width = 6f)
            )

            // Draw hour marks
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
                    color = SLText.copy(alpha = if (i % 3 == 0) 0.8f else 0.4f),
                    start = start,
                    end = end,
                    strokeWidth = if (i % 3 == 0) 4f else 2f,
                    cap = StrokeCap.Round
                )
            }

            // Get time
            val hour = calendar.get(Calendar.HOUR)
            val minute = calendar.get(Calendar.MINUTE)
            val second = calendar.get(Calendar.SECOND)

            // Angles
            val hourAngle = ((hour + minute / 60f) * 30 - 90) * (PI / 180)
            val minuteAngle = ((minute + second / 60f) * 6 - 90) * (PI / 180)
            val secondAngle = (second * 6 - 90) * (PI / 180)

            // Hour hand
            drawLine(
                color = Color(0xFF6C63FF),
                start = center,
                end = Offset(
                    x = (center.x + cos(hourAngle) * (radius * 0.45f)).toFloat(),
                    y = (center.y + sin(hourAngle) * (radius * 0.45f)).toFloat()
                ),
                strokeWidth = 8f,
                cap = StrokeCap.Round
            )
            // Minute hand
            drawLine(
                color = SLText,
                start = center,
                end = Offset(
                    x = (center.x + cos(minuteAngle) * (radius * 0.65f)).toFloat(),
                    y = (center.y + sin(minuteAngle) * (radius * 0.65f)).toFloat()
                ),
                strokeWidth = 5f,
                cap = StrokeCap.Round
            )
            // Second hand
            drawLine(
                color = Color(0xFF6C63FF).copy(alpha = 0.7f),
                start = center,
                end = Offset(
                    x = (center.x + cos(secondAngle) * (radius * 0.75f)).toFloat(),
                    y = (center.y + sin(secondAngle) * (radius * 0.75f)).toFloat()
                ),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )
            // Center dot
            drawCircle(
                color = Color(0xFF6C63FF),
                radius = 8.dp.toPx(),
                center = center
            )
        }
    }
}
