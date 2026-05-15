package com.sumit.launcher.ui.presentation.homescreen.component

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sumit.sololevelinglauncher.ui.theme.neumorphicSurface

@Composable
fun UsageAccessPrompt(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .neumorphicSurface(
                shape = RoundedCornerShape(16.dp),
                elevation = 10.dp,
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            .clickable {
                context.startActivity(
                    Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Usage chart",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = "Tap to grant usage access",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
