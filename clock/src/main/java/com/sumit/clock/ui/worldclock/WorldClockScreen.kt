package com.sumit.clock.ui.worldclock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumit.clock.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun WorldClockScreen(
    viewModel: WorldClockViewModel = hiltViewModel()
) {
    val zoneIds by viewModel.zoneIds.collectAsState()
    var showPicker by remember { mutableStateOf(false) }
    var nowMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (isActive) {
            nowMillis = System.currentTimeMillis()
            delay(30_000L)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        if (zoneIds.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.world_clock_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(zoneIds, key = { it }) { zoneId ->
                    CityRow(
                        zoneId = zoneId,
                        nowMillis = nowMillis,
                        onRemove = { viewModel.remove(zoneId) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { showPicker = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.world_clock_add))
        }
    }

    if (showPicker) {
        TimeZonePickerDialog(
            existing = zoneIds.toSet(),
            onDismiss = { showPicker = false },
            onPick = {
                viewModel.add(it)
                showPicker = false
            }
        )
    }
}

@Composable
private fun CityRow(
    zoneId: String,
    nowMillis: Long,
    onRemove: () -> Unit
) {
    val tz = remember(zoneId) { TimeZone.getTimeZone(zoneId) }
    val timeFormat = remember(zoneId) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).apply { timeZone = tz }
    }
    val timeText = timeFormat.format(Date(nowMillis))
    val offsetText = formatOffsetVsLocal(tz, nowMillis)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = prettyCityName(zoneId),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = offsetText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = timeText,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onSurface
        )
        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.world_clock_remove),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

internal fun prettyCityName(zoneId: String): String {
    val last = zoneId.substringAfterLast('/', zoneId)
    return last.replace('_', ' ')
}

private fun formatOffsetVsLocal(tz: TimeZone, nowMillis: Long): String {
    val local = TimeZone.getDefault()
    val diffMin = (tz.getOffset(nowMillis) - local.getOffset(nowMillis)) / 60_000
    if (diffMin == 0) return "Local"
    val ahead = diffMin > 0
    val abs = kotlin.math.abs(diffMin)
    val h = abs / 60
    val m = abs % 60
    val parts = buildString {
        if (h > 0) append("${h}h")
        if (m > 0) {
            if (h > 0) append(' ')
            append("${m}m")
        }
    }
    val sign = if (ahead) "+" else "-"
    val direction = if (ahead) "ahead" else "behind"
    return "$sign$parts $direction"
}
