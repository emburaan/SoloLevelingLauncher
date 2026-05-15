package com.sumit.launcher.ui.presentation.searchscreen.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.ceil

@Composable
fun FocusPromptDialog(
    appLabel: String,
    countdownSeconds: Int,
    usedMinutes: Int? = null,
    limitMinutes: Int? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val progress = remember(countdownSeconds) { Animatable(0f) }
    LaunchedEffect(countdownSeconds) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = countdownSeconds * 1000,
                easing = LinearEasing
            )
        )
    }
    val progressValue = progress.value
    val remaining = ceil((1f - progressValue) * countdownSeconds).toInt().coerceAtLeast(0)
    val isReady = progressValue >= 1f

    val isLimitPrompt = limitMinutes != null
    val title = if (isLimitPrompt) "Daily limit reached" else "Take a breath"
    val subtitle = if (isLimitPrompt) {
        "You've used $appLabel for ${usedMinutes ?: 0} of $limitMinutes min today."
    } else {
        "Pause before opening $appLabel."
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(text = title, color = MaterialTheme.colorScheme.onSurface)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier.size(72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { progressValue },
                        modifier = Modifier.size(72.dp),
                        strokeWidth = 4.dp,
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Text(
                        text = if (isReady) "Go" else remaining.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onDismiss) { Text("Cancel") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onConfirm,
                    enabled = isReady,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) { Text("Open") }
            }
        }
    )
}
