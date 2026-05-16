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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.sumit.launcher.R
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
    val title = stringResource(
        if (isLimitPrompt) R.string.focus_prompt_title_limit
        else R.string.focus_prompt_title_default
    )
    val subtitle = if (isLimitPrompt) {
        stringResource(
            R.string.focus_prompt_subtitle_limit,
            appLabel,
            usedMinutes ?: 0,
            limitMinutes
        )
    } else {
        stringResource(R.string.focus_prompt_subtitle_default, appLabel)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_dialog)),
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
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_5xl)))
                Box(
                    modifier = Modifier.size(dimensionResource(R.dimen.focus_prompt_progress)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { progressValue },
                        modifier = Modifier.size(dimensionResource(R.dimen.focus_prompt_progress)),
                        strokeWidth = dimensionResource(R.dimen.progress_stroke),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Text(
                        text = if (isReady) stringResource(R.string.focus_prompt_ready)
                        else remaining.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.action_cancel))
                }
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_md)))
                Button(
                    onClick = onConfirm,
                    enabled = isReady,
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_chip)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) { Text(stringResource(R.string.action_open)) }
            }
        }
    )
}
