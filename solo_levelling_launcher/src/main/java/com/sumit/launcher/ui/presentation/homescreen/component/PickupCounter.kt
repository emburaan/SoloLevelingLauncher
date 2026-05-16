package com.sumit.launcher.ui.presentation.homescreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.sumit.launcher.R
import com.sumit.launcher.data.usage.TodayScreenStats
import com.sumit.sololevelinglauncher.ui.theme.neumorphicSurface

@Composable
fun PickupCounter(
    stats: TodayScreenStats,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .neumorphicSurface(
                shape = RoundedCornerShape(dimensionResource(R.dimen.corner_card)),
                elevation = dimensionResource(R.dimen.elevation_card),
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            .padding(
                horizontal = dimensionResource(R.dimen.card_padding_h),
                vertical = dimensionResource(R.dimen.card_padding_v)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stats.pickups.toString(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = stringResource(R.string.pickup_label),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
