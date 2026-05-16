package com.sumit.launcher.ui.presentation.homescreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.launcher.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun FocusModeIndicator(
    modifier: Modifier = Modifier,
    viewModel: FocusBlocksViewModel = hiltViewModel()
) {
    val blocks by viewModel.blocks.collectAsState()
    var tick by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (isActive) {
            tick = System.currentTimeMillis()
            delay(60_000)
        }
    }

    val activeBlock = remember(blocks, tick) {
        blocks.firstOrNull { it.isActive(tick) }
    } ?: return

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_pill)))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(
                    horizontal = dimensionResource(R.dimen.pill_padding_h),
                    vertical = dimensionResource(R.dimen.pill_padding_v)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md))
        ) {
            Text(
                text = stringResource(R.string.focus_mode_active),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "${formatHm(activeBlock.startMinute)}–${formatHm(activeBlock.endMinute)}",
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))
    }
}

private fun formatHm(minutesSinceMidnight: Int): String {
    val h = (minutesSinceMidnight / 60).coerceIn(0, 23)
    val m = (minutesSinceMidnight % 60).coerceIn(0, 59)
    return "%02d:%02d".format(h, m)
}
