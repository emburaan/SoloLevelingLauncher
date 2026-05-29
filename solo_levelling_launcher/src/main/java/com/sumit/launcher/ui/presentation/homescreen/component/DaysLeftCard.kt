package com.sumit.launcher.ui.presentation.homescreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.sumit.launcher.R
import com.sumit.launcher.ui.theme.neumorphicSurface
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.Calendar

private val UrgentRed = Color(0xFFEF4444)

@Composable
fun DaysLeftCard(modifier: Modifier = Modifier) {
    var tick by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(60_000)
            tick = System.currentTimeMillis()
        }
    }

    val cal = remember(tick) { Calendar.getInstance() }
    val totalDays = cal.getActualMaximum(Calendar.DAY_OF_YEAR)
    val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
    val daysLeft = totalDays - dayOfYear
    val year = cal.get(Calendar.YEAR)

    Column(
        modifier = modifier
            .neumorphicSurface(
                shape = RoundedCornerShape(dimensionResource(R.dimen.corner_card)),
                elevation = dimensionResource(R.dimen.elevation_card),
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            .padding(
                horizontal = dimensionResource(R.dimen.date_card_padding_h),
                vertical = dimensionResource(R.dimen.card_padding_v)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = daysLeft.toString(),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = UrgentRed
        )
        Text(
            text = stringResource(R.string.days_left_label, year),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
