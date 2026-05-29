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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.sumit.launcher.R
import com.sumit.launcher.ui.theme.neumorphicSurface
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.Calendar
import java.util.Locale

@Composable
fun DateCard(modifier: Modifier = Modifier) {
    var tick by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(60_000)
            tick = System.currentTimeMillis()
        }
    }

    val cal = remember(tick) { Calendar.getInstance() }
    val day = cal.get(Calendar.DAY_OF_MONTH)
    val weekday = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
        ?.uppercase(Locale.getDefault()).orEmpty()
    val month = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
        ?.uppercase(Locale.getDefault()).orEmpty()

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
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = weekday,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = day.toString(),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = month,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
