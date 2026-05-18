package com.sumit.launcher.ui.presentation.homescreen.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.sumit.launcher.R

@Composable
fun FocusCheckDialog(
    minutes: Int,
    onContinue: () -> Unit,
    onStepAway: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onContinue,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_dialog)),
        title = {
            Text(
                text = stringResource(R.string.focus_check_title),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = stringResource(R.string.focus_check_body, minutes),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            TextButton(onClick = onContinue) {
                Text(stringResource(R.string.focus_check_action_continue))
            }
        },
        dismissButton = {
            TextButton(onClick = onStepAway) {
                Text(stringResource(R.string.focus_check_action_step_away))
            }
        }
    )
}
