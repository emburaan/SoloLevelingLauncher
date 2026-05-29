package com.sumit.launcher.ui.presentation.homescreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sumit.launcher.R
import com.sumit.launcher.data.usage.DayUsage
import com.sumit.launcher.ui.theme.SLAccentBlue
import com.sumit.launcher.ui.theme.SLAccentPurple
import com.sumit.launcher.ui.theme.neumorphicSurface

@Composable
fun UsageBarChart(usageData: List<DayUsage>, modifier: Modifier = Modifier) {
    val maxMinutes = (usageData.maxOfOrNull { it.minutes } ?: 1).coerceAtLeast(1).toFloat()
    val gradient = Brush.horizontalGradient(listOf(SLAccentPurple, SLAccentBlue))
    val barMaxWidth = dimensionResource(R.dimen.chart_bar_max_width)
    val barBase = dimensionResource(R.dimen.spacing_sm)
    Column(
        modifier = modifier
            .neumorphicSurface(
                shape = RoundedCornerShape(dimensionResource(R.dimen.corner_card_lg)),
                elevation = dimensionResource(R.dimen.elevation_card_md),
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            .padding(
                horizontal = dimensionResource(R.dimen.spacing_xl),
                vertical = dimensionResource(R.dimen.card_padding_v)
            )
    ) {
        Text(
            text = stringResource(R.string.chart_last_seven_days),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_sm))
        )
        usageData.forEachIndexed { idx, day ->
            val isToday = idx == usageData.lastIndex
            val barRatio = day.minutes / maxMinutes
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(vertical = 3.dp)
            ) {
                Text(
                    day.label,
                    color = if (isToday) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isToday) FontWeight.Bold else null,
                    modifier = Modifier.width(dimensionResource(R.dimen.chart_day_label_width))
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_md)))
                Box(
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.chart_bar_height))
                        .width(barMaxWidth * barRatio + barBase)
                        .background(gradient, RoundedCornerShape(3.dp))
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_sm)))
                Text(
                    text = formatDuration(day.minutes),
                    color = if (isToday) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface,
                    style = if (isToday) MaterialTheme.typography.labelMedium
                    else MaterialTheme.typography.labelSmall,
                    fontWeight = if (isToday) FontWeight.Bold else null
                )
            }
        }
    }
}

private fun formatDuration(minutes: Int): String {
    if (minutes <= 0) return "0m"
    val h = minutes / 60
    val m = minutes % 60
    return when {
        h == 0 -> "${m}m"
        m == 0 -> "${h}h"
        else -> "${h}h${m}m"
    }
}
