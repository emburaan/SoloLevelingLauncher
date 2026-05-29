package com.sumit.launcher.ui.presentation.homescreen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.sumit.launcher.R
import com.sumit.launcher.ui.theme.neumorphicSurface

@Composable
fun SettingsCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .neumorphicSurface(
                shape = RoundedCornerShape(dimensionResource(R.dimen.corner_card)),
                elevation = dimensionResource(R.dimen.elevation_card),
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = stringResource(R.string.content_desc_settings),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
