package com.sumit.launcher.ui.presentation.homescreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sumit.sololevelinglauncher.ui.theme.SLText

@Composable
fun UsageBarChart(usageData: List<Int>, modifier: Modifier = Modifier) {
    val maxUsage = (usageData.maxOrNull() ?: 1).toFloat()
    val days = listOf("S", "M", "T", "W", "T", "F", "S")
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF6C63FF), Color(0xFF42A5F5))
    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            usageData.forEachIndexed { i, usage ->
                val barWidthRatio = if (maxUsage == 0f) 0f else usage / maxUsage
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(days[i], color = SLText, modifier = Modifier.width(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .width((barWidthRatio * 60).dp + 8.dp)
                            .background(
                                brush = gradient,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
        }
    }
}