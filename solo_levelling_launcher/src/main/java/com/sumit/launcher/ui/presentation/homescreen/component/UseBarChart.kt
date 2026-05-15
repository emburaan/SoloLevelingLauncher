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
import androidx.compose.ui.unit.dp
import com.sumit.launcher.data.usage.DayUsage
import com.sumit.sololevelinglauncher.ui.theme.SLAccentBlue
import com.sumit.sololevelinglauncher.ui.theme.SLAccentPurple
import com.sumit.sololevelinglauncher.ui.theme.neumorphicSurface

@Composable
fun UsageBarChart(usageData: List<DayUsage>, modifier: Modifier = Modifier) {
    val maxMinutes = (usageData.maxOfOrNull { it.minutes } ?: 1).coerceAtLeast(1).toFloat()
    val gradient = Brush.horizontalGradient(listOf(SLAccentPurple, SLAccentBlue))
    Column(
        modifier = modifier
            .neumorphicSurface(
                shape = RoundedCornerShape(20.dp),
                elevation = 12.dp,
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Last 7 days",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        usageData.forEach { day ->
            val barRatio = day.minutes / maxMinutes
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(vertical = 3.dp)
            ) {
                Text(
                    day.label,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(14.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .width((barRatio * 64).dp + 6.dp)
                        .background(gradient, RoundedCornerShape(3.dp))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = formatDuration(day.minutes),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelSmall
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
