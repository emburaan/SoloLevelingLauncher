package com.sumit.clock.ui.alarm

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Mirrors com.sumit.launcher.ui.theme so the alarm ring screen reads as the
// same app even though the clock library can't depend on the launcher module.
internal val AlarmAccentPurple = Color(0xFF6C63FF)
internal val AlarmAccentBlue = Color(0xFF42A5F5)
internal val AlarmAccentCyan = Color(0xFF22D3EE)
internal val AlarmBackground = Color(0xFF0F1117)
internal val AlarmSurface = Color(0xFF161924)
internal val AlarmSurfaceHigh = Color(0xFF1D2130)
internal val AlarmSurfaceHigher = Color(0xFF252A3B)
internal val AlarmOnSurface = Color(0xFFE8EAF1)
internal val AlarmOnSurfaceVariant = Color(0xFF9CA0B0)
internal val AlarmOutline = Color(0xFF3A3F52)

private val AlarmColorScheme = darkColorScheme(
    primary = AlarmAccentPurple,
    onPrimary = Color.White,
    primaryContainer = AlarmSurfaceHigher,
    onPrimaryContainer = AlarmOnSurface,
    secondary = AlarmAccentBlue,
    onSecondary = Color.White,
    tertiary = AlarmAccentCyan,
    onTertiary = Color(0xFF052430),
    background = AlarmBackground,
    onBackground = AlarmOnSurface,
    surface = AlarmSurface,
    onSurface = AlarmOnSurface,
    surfaceVariant = AlarmSurfaceHigh,
    onSurfaceVariant = AlarmOnSurfaceVariant,
    surfaceContainerLowest = AlarmBackground,
    surfaceContainerLow = AlarmSurface,
    surfaceContainer = AlarmSurfaceHigh,
    surfaceContainerHigh = AlarmSurfaceHigher,
    surfaceContainerHighest = AlarmSurfaceHigher,
    outline = AlarmOutline,
    outlineVariant = AlarmSurfaceHigh,
    scrim = Color.Black
)

private val AlarmTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )
)

@Composable
internal fun AlarmTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AlarmColorScheme,
        typography = AlarmTypography,
        content = content
    )
}
