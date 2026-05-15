package com.sumit.sololevelinglauncher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/** Soft shadow + highlight tones used by neumorphic surfaces. */
data class NeumorphicTones(
    val shadow: Color,
    val highlight: Color,
    val accentGlow: Color
)

val LocalNeumorphicTones = compositionLocalOf {
    NeumorphicTones(
        shadow = Color.Black,
        highlight = Color.White,
        accentGlow = SLAccentPurple
    )
}

object SLTheme {
    val tones: NeumorphicTones
        @Composable @ReadOnlyComposable
        get() = LocalNeumorphicTones.current
}

private val DarkColorScheme = darkColorScheme(
    primary = SLAccentPurple,
    onPrimary = Color.White,
    primaryContainer = DarkSurfaceHigher,
    onPrimaryContainer = DarkOnSurface,
    secondary = SLAccentBlue,
    onSecondary = Color.White,
    tertiary = SLAccentCyan,
    onTertiary = Color(0xFF052430),
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceHigh,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceContainerLowest = DarkBackground,
    surfaceContainerLow = DarkSurface,
    surfaceContainer = DarkSurfaceHigh,
    surfaceContainerHigh = DarkSurfaceHigher,
    surfaceContainerHighest = DarkSurfaceHigher,
    outline = DarkHighlight,
    outlineVariant = DarkSurfaceHigh,
    scrim = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = SLAccentPurple,
    onPrimary = Color.White,
    primaryContainer = LightSurfaceHigher,
    onPrimaryContainer = LightOnSurface,
    secondary = SLAccentBlue,
    onSecondary = Color.White,
    tertiary = SLAccentCyan,
    onTertiary = Color(0xFF052430),
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceHigh,
    onSurfaceVariant = LightOnSurfaceVariant,
    surfaceContainerLowest = LightBackground,
    surfaceContainerLow = LightSurface,
    surfaceContainer = LightSurfaceHigh,
    surfaceContainerHigh = LightSurfaceHigher,
    surfaceContainerHighest = LightSurfaceHigher,
    outline = LightShadow,
    outlineVariant = LightSurfaceHigh,
    scrim = Color(0xFF202126)
)

@Composable
fun SoloLevelingLauncherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    @Suppress("UNUSED_PARAMETER") dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val tones = if (darkTheme) {
        NeumorphicTones(
            shadow = DarkShadow,
            highlight = DarkHighlight,
            accentGlow = SLAccentPurple
        )
    } else {
        NeumorphicTones(
            shadow = LightShadow,
            highlight = LightHighlight,
            accentGlow = SLAccentPurple
        )
    }

    CompositionLocalProvider(LocalNeumorphicTones provides tones) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
