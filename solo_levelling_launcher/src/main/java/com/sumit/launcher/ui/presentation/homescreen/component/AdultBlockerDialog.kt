package com.sumit.launcher.ui.presentation.homescreen.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.sumit.launcher.R

private const val PRIVATE_DNS_ACTION = "android.settings.PRIVATE_DNS_SETTINGS"

@Composable
fun AdultBlockerDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_dialog)),
        title = { Text(stringResource(R.string.adult_blocker_dialog_title)) },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.adult_blocker_dialog_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_3xl)))

                ProviderRow(
                    name = stringResource(R.string.adult_blocker_provider_cloudflare),
                    hostname = stringResource(R.string.adult_blocker_hostname_cloudflare),
                    onCopy = { copyToClipboard(context, it) }
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_md)))
                ProviderRow(
                    name = stringResource(R.string.adult_blocker_provider_adguard),
                    hostname = stringResource(R.string.adult_blocker_hostname_adguard),
                    onCopy = { copyToClipboard(context, it) }
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_md)))
                ProviderRow(
                    name = stringResource(R.string.adult_blocker_provider_cleanbrowsing),
                    hostname = stringResource(R.string.adult_blocker_hostname_cleanbrowsing),
                    onCopy = { copyToClipboard(context, it) }
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_3xl)))
                Text(
                    text = stringResource(R.string.adult_blocker_steps_title),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.adult_blocker_steps_body),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { openPrivateDnsSettings(context) }) {
                Text(stringResource(R.string.adult_blocker_open_settings))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_close))
            }
        }
    )
}

@Composable
private fun ProviderRow(
    name: String,
    hostname: String,
    onCopy: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = hostname,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        TextButton(onClick = { onCopy(hostname) }) {
            Text(stringResource(R.string.adult_blocker_copy))
        }
    }
}

private fun copyToClipboard(context: Context, hostname: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        ?: return
    clipboard.setPrimaryClip(ClipData.newPlainText("private-dns-hostname", hostname))
    Toast.makeText(
        context,
        context.getString(R.string.adult_blocker_copied, hostname),
        Toast.LENGTH_SHORT
    ).show()
}

private fun openPrivateDnsSettings(context: Context) {
    val direct = Intent(PRIVATE_DNS_ACTION).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val resolved = context.packageManager.resolveActivity(direct, 0)
    val intent = if (resolved != null) {
        direct
    } else {
        Intent(Settings.ACTION_WIRELESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    runCatching { context.startActivity(intent) }
}
