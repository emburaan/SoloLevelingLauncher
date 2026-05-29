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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.sumit.launcher.R
import com.sumit.launcher.ui.theme.neumorphicSurface

@Composable
fun UsageAccessPrompt(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .neumorphicSurface(
                shape = RoundedCornerShape(dimensionResource(R.dimen.corner_card)),
                elevation = dimensionResource(R.dimen.elevation_card),
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            .clickable {
                context.startActivity(
                    Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            .padding(
                horizontal = dimensionResource(R.dimen.spacing_xl),
                vertical = dimensionResource(R.dimen.spacing_xl)
            )
    ) {
        Text(
            text = stringResource(R.string.usage_chart_label),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = stringResource(R.string.usage_chart_tap_hint),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
